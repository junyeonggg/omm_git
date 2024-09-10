package com.omm.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omm.dto.CartDto;
import com.omm.dto.CategoryDto;
import com.omm.dto.CommentDto;
import com.omm.dto.FoodDto;
import com.omm.dto.MemberDto;
import com.omm.service.CategoryService;
import com.omm.service.MemberService;
import com.omm.service.RecipeService;
import com.omm.service.ShopService;

@Controller
public class ShopController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private MemberService memberService;

	@GetMapping("/shop")
	public String shop(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "searchCategory", required = false) String searchCategory,
			@RequestParam(value = "page", defaultValue = "1") String pageParam, // 기본값을 1로 설정
			Model model) {

		int pageSize = 20; // 한 페이지에 표시할 상품 수
		int page = 1; // 기본 페이지 번호를 1로 설정

		// pageParam을 정수로 변환하는 로직 추가
		try {
			page = Integer.parseInt(pageParam);
			if (page < 1) {
				page = 1; // 페이지 번호가 1보다 작으면 1로 설정
			}
		} catch (NumberFormatException e) {
			// 예외 발생 시 페이지 번호를 1로 설정
			page = 1;
		}

		int offset = (page - 1) * pageSize; // 1-based page를 0-based offset으로 변환

		// 데이터 로드
		List<FoodDto> foods;
		if (query != null && !query.trim().isEmpty()) {
			foods = shopService.searchFoods(query, null, pageSize, offset);
		} else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
			foods = shopService.searchFoods(null, searchCategory, pageSize, offset);
		} else {
			foods = shopService.getFoods(pageSize, offset);
		}

		int totalItems = shopService.getTotalItems(query, searchCategory);
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		List<CategoryDto> categories = shopService.findAllCategories();

		// 네비게이션 범위 설정
		int startPage = ((page - 1) / 10) * 10 + 1; // 현재 페이지를 기준으로 시작 페이지 계산
		int endPage = Math.min(startPage + 9, totalPages); // 10개씩 페이지 버튼 표시

		// 모델에 값 추가
		model.addAttribute("foods", foods);
		model.addAttribute("categories", categories);
		model.addAttribute("currentPage", page); // 현재 페이지 번호 (1부터 시작)
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("query", query);
		model.addAttribute("searchCategory", searchCategory);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "shop";
	}

	@GetMapping("/product/{foodProductId}")
	public String productDetail(@PathVariable("foodProductId") String foodProductId, Model model, Principal principal) {
		FoodDto food = shopService.getFoodById(foodProductId);
		System.out.println(food.toString());
		if (food == null) {
			model.addAttribute("errorMessage", "해당 상품을 찾을 수 없습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}
		// Debug 로그 추가
		System.out.println("Food Name: " + food.getFoodName());
		System.out.println("Food Price: " + food.getFoodLprice());
		System.out.println("Food Image: " + food.getFoodImg());
		String user_nickname = "";
		try {
			user_nickname = recipeService.getUserNicknameByUserId(principal.getName());
//			System.out.println("user_nickname : "+user_nickname);
			model.addAttribute("user_nickname", user_nickname);
			model.addAttribute("user_id", principal.getName());
		} catch (Exception e) {
			model.addAttribute("user_nickname", "Null");
		}
		model.addAttribute("food", food);

		// 댓글 리스트
		// 해당 레시피의 댓글
		List<CommentDto> comment_list = recipeService.getCommentsByTargetIdAndRefType(food.getFoodId(), 2);
		model.addAttribute("comment_list", comment_list);

		return "product";
	}

	@ResponseBody
	@PostMapping("/addreply")
	public String addReply(CommentDto comment) {
		// user_id
		// comment_content
		// comment_create_date
		// target_id
		// parent_comment_id
		// comment_rating
		// reference_type
		System.out.println(comment.toString());

		shopService.insertReply(comment);

		return "";
	}

	@GetMapping("/cart")
	public String cartPage(Principal principal, Model model) {
		List<HashMap<String, Object>> cart_list = shopService.getCartByUserId(principal.getName());
		cart_list.forEach((cart) -> {
			cart.replace("food_lprice", Integer.valueOf((String) cart.get("food_lprice")));
		});

		model.addAttribute("cart_list", cart_list);
		return "cart";
	}

	@ResponseBody
	@PostMapping("/cart/insert")
	public boolean insertCart(CartDto cartDto) {
		System.out.println(cartDto.toString());
		boolean check = shopService.checkCartByUserIdAndFoodId(cartDto);
		if (check) {
			// 이미 있으면
			return check;
		} else {
			// 없으면 넣고
			shopService.insertCart(cartDto);
			return check;
		}
	}

	@ResponseBody
	@PostMapping("/delete/cart")
	public String deleteFromCart(@RequestParam("cart_id") int cart_id) {
		shopService.deleteCartByCartId(cart_id);

		return "";
	}

	@ResponseBody
	@PostMapping("/changeCheck")
	public String changeCheck(@RequestParam("check") boolean check, @RequestParam("food_id") int food_id,
			Principal principal) {
		shopService.changeCheck(check, food_id, principal.getName());
		return "";
	}

	@GetMapping("/order")
	public String orderPage(Model model, Principal principal) {
		List<HashMap<String, Object>> cart_list = shopService.getCartByUserIdAndCheck(principal.getName());
		cart_list.forEach((cart) -> {
			cart.replace("food_lprice", Integer.valueOf((String) cart.get("food_lprice")));
		});
		MemberDto member = memberService.getMemberByUserId(principal.getName());
		model.addAttribute("member", member);
		model.addAttribute("cart_list", cart_list);
		return "order";
	}

}
