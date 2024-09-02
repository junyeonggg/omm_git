package com.omm.service;

import com.omm.dao.ShopDao;
import com.omm.dto.FoodDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopDao shopDao;

    public ShopService(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    // 카테고리별 추천 상품 조회
    public List<FoodDto> findTopNByCategoryId(int categoryId, int limit) {
        return shopDao.findTopNByCategoryId(categoryId, limit);
    }

    // 검색어와 카테고리에 따라 음식 목록 조회
    public List<FoodDto> searchFoods(String query, String searchCategory) {
        if (searchCategory != null && !searchCategory.isEmpty()) {
            return shopDao.searchFoodsByCategory(query, searchCategory);
        } else {
            return shopDao.searchFoods(query);
        }
    }
}
