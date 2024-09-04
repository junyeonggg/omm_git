package com.omm.service;

import com.omm.dao.CategoryDao;
import com.omm.dao.ShopDao;
import com.omm.dto.CategoryDto;
import com.omm.dto.FoodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
                int categoryId = Integer.parseInt(searchCategory);  // searchCategory를 정수형으로 변환
                return shopDao.findFoodsByCategoryWithSubCategories(categoryId, pageSize, offset);
            } catch (NumberFormatException e) {
                return List.of();  // 잘못된 카테고리 ID일 경우 빈 리스트 반환
            }
        } else {
            return List.of();  // 검색어와 카테고리 모두 없는 경우 빈 리스트 반환
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
}