package com.omm.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.ShopDao;
import com.omm.dto.CartDto;
import com.omm.dto.CategoryDto;
import com.omm.dto.CommentDto;
import com.omm.dto.FoodDto;
import com.omm.dto.OrderDto;

@Service
public class ShopService {

	@Autowired
	private ShopDao shopDao;

	@Autowired
	private CategoryService categoryService;

	public List<FoodDto> getFoods(int pageSize, int offset) {
		return shopDao.findAll(pageSize, offset);
	}

	public List<FoodDto> searchFoods(String query, String searchCategory, int pageSize, int offset) {
		if (query != null && !query.trim().isEmpty()) {
			return shopDao.searchFoods(query, pageSize, offset);
		} else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
			try {
				int categoryId = Integer.parseInt(searchCategory); // searchCategory를 정수형으로 변환
				return shopDao.findFoodsByCategoryWithSubCategories(categoryId, pageSize, offset);
			} catch (NumberFormatException e) {
				return List.of(); // 잘못된 카테고리 ID일 경우 빈 리스트 반환
			}
		} else {
			return List.of(); // 검색어와 카테고리 모두 없는 경우 빈 리스트 반환
		}
	}

	public int getTotalItems(String query, String searchCategory) {
		if (query != null && !query.trim().isEmpty()) {
			return shopDao.countFoodsByQuery(query);
		} else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
			try {
				int categoryId = Integer.parseInt(searchCategory);
				return shopDao.countFoodsByCategory(categoryId);
			} catch (NumberFormatException e) {
				return 0;
			}
		} else {
			return shopDao.countAllFoods();
		}
	}

	public List<CategoryDto> findAllCategories() {
		return categoryService.findAllCategories();
	}

	public FoodDto getFoodById(String foodProductId) {
		return shopDao.getFoodById(foodProductId);
	}

	public void insertReply(CommentDto comment) {
		shopDao.insertReply(comment);
	}

	public void insertCart(CartDto cartDto) {
		shopDao.insertCart(cartDto);
	}

	public List<HashMap<String, Object>> getCartByUserId(String user_id) {
		return shopDao.getCartByUserId(user_id);
	}

	public void deleteCartByCartId(int cart_id) {
		shopDao.deleteCartByCartId(cart_id);
	}

	public boolean checkCartByUserIdAndFoodId(CartDto cartDto) {
		int cnt = shopDao.checkCartByUserIdAndFoodId(cartDto);
		if (cnt > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void changeCheck(boolean check, int food_id, String user_id) {
		if (check) {
			shopDao.changecheck(1, food_id, user_id);
		} else {
			shopDao.changecheck(0, food_id, user_id);
		}
	}
	public void changeCnt(int quantity, int food_id, String user_id) {
		shopDao.changeCnt(quantity,food_id,user_id);
	}

	public List<HashMap<String, Object>> getCartByUserIdAndCheck(String name) {
		return shopDao.getCartByUserIdAndCheck(name);
	}

	public void insertOrder(OrderDto orderDto, List<String> food_id_list) {
		orderDto.orderDate(orderDto);
		food_id_list.forEach((food_id) -> {
			food_id = food_id.replace("[", "");
			food_id = food_id.replace("]", "");
			orderDto.setFood_id(Integer.valueOf(food_id));
			shopDao.insertOrder(orderDto);
		});
	}

	public List<HashMap<String, Object>> getFoodOrder(int food_id) {
		return shopDao.getFoodOrder(food_id);
	}


	public boolean updateReview(int commentId, String newContent) {
		try {
			shopDao.updateReview(newContent, commentId);
			return true; // 성공적으로 업데이트됨
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
			return false; // 업데이트 실패
		}
	}

	public boolean deleteReview(int commentId) {
		try {
			shopDao.deleteReview(commentId);
			return true; // 성공적으로 삭제됨
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
			return false; // 삭제 실패
		}
	}

    public void resetCartByUserId(String user_id) {
		shopDao.resetCartByUserId(user_id);
    }

	public List<HashMap<String, Object>> selectOrderHisByUserId(String user_id) {
		List<HashMap<String, Object>> orderHistList = shopDao.selectOrderHisByUserId(user_id);
		orderHistList.forEach(d->{
			System.out.println("수정전 : "+d.get("order_date"));
			String time =d.get("order_date").toString();
			time = time.replace("T"," ");
			d.put("order_date",time);
			System.out.println("수정 후 : "+d.get("order_date"));
		});
		return orderHistList;
	}
}