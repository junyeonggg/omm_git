package com.omm.controller;

import com.omm.client.NaverShopClient;
import com.omm.dto.CategoryDto;
import com.omm.dto.FoodDto;
import com.omm.dto.NaverShopResponse;
import com.omm.service.CategoryService;
import com.omm.service.ShopService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {

    private final NaverShopClient naverShopClient;
    private final ShopService shopService;
    private final CategoryService categoryService;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    public ShopController(NaverShopClient naverShopClient, ShopService shopService, CategoryService categoryService) {
        this.naverShopClient = naverShopClient;
        this.shopService = shopService;
        this.categoryService = categoryService;
    }

    @GetMapping("/shop")
    public String getShopPage(@RequestParam(value = "query", required = false) String query, Model model) {
        System.out.println("접속");

        if (query == null || query.trim().isEmpty()) {
            query = "과일"; // 기본 검색어
        }

        NaverShopResponse response = naverShopClient.searchShop(query, 100, clientId, clientSecret);

        // 디버깅을 위해 응답과 리스트 출력
        System.out.println("응답: " + response);
        if (response != null) {
            System.out.println("응답 아이템: " + response.getItems());
        }
        // 응답에서 FoodDto 리스트를 추출
        List<FoodDto> foodList = (response != null && response.getItems() != null) ? response.getItems() : List.of();

        // 카테고리 및 음식 데이터를 데이터베이스에 저장
        for (FoodDto food : foodList) {
            // 각 카테고리 레벨별로 저장하고 관계 설정
            CategoryDto category1 = categoryService.saveCategory(food.getFoodCategory1(), null);
            CategoryDto category2 = categoryService.saveCategory(food.getFoodCategory2(), category1.getCategory_id());
            CategoryDto category3 = categoryService.saveCategory(food.getFoodCategory3(), category2.getCategory_id());
            CategoryDto category4 = categoryService.saveCategory(food.getFoodCategory4(), category3.getCategory_id());

            // 가장 하위 카테고리 ID를 food에 설정
            food.setCategoryId(category4.getCategory_id());

            // 음식 데이터 저장
            shopService.saveFood(food);
        }

        model.addAttribute("foods", foodList);

        return "shop";
    }
}
