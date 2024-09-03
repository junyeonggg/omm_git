package com.omm.controller;

import com.omm.dto.FoodDto;
import com.omm.dto.CategoryDto;
import com.omm.service.ShopService;
import com.omm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "query", required = false) String query,
                       @RequestParam(value = "searchCategory", required = false) String searchCategory,
                       Model model) {

        List<FoodDto> foods;

        if (query != null && !query.trim().isEmpty()) {
            foods = shopService.searchFoods(query, null);
        } else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
            foods = shopService.searchFoods(null, searchCategory);
        } else {
            foods = Collections.emptyList();  // 기본값으로 빈 리스트 반환
        }

        // 계층 구조로 된 카테고리 데이터 로드
        List<CategoryDto> categories = categoryService.findAllCategories();

        model.addAttribute("foods", foods);
        model.addAttribute("categories", categories);

        return "shop";
    }

    @GetMapping("/product/{foodProductId}")
    public String productDetail(@PathVariable("foodProductId") String foodProductId, Model model) {
        FoodDto food = shopService.getFoodById(foodProductId);
        model.addAttribute("food", food);
        return "product";
    }
}

