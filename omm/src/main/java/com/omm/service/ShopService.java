package com.omm.service;

import com.omm.dao.ShopDao;
import com.omm.dto.FoodDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopService {

    private final ShopDao shopDao;

    public ShopService(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    @Transactional
    public void saveFood(FoodDto food) {
        shopDao.insertFood(food);
    }

    @Transactional
    public void saveAllFoods(List<FoodDto> foods) {
        shopDao.insertFoods(foods);
    }

}
