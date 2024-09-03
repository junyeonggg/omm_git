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

    public List<FoodDto> searchFoods(String query, String searchCategory) {
        if (query != null && !query.isEmpty()) {
            return shopDao.searchFoods(query);
        } else if (searchCategory != null && !searchCategory.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(searchCategory);  // searchCategory를 정수형으로 변환
                return shopDao.findFoodsByCategoryWithSubCategories(categoryId);  // 하위 카테고리 포함 검색
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();  // 기본값으로 빈 리스트 반환
        }
    }

    public List<CategoryDto> findAllCategories() {
        return categoryService.findAllCategories();
    }
    public FoodDto getFoodById(String foodProductId) {
        return shopDao.getFoodById(foodProductId);
    }
}