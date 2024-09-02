package com.omm.controller;

import com.omm.dto.FoodDto;
import com.omm.dto.CategoryDto;
import com.omm.service.ShopService;
import com.omm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
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

        // 배너 이미지 URL 리스트
        List<String> bannerImages = Arrays.asList(
                "/images/banner1.jpg",
                "/images/banner2.jpg",
                "/images/banner3.jpg"
        );

        model.addAttribute("bannerImages", bannerImages);

        // 검색 결과
        List<FoodDto> foods;
        if (searchCategory != null && !searchCategory.isEmpty()) {
            foods = shopService.searchFoods(query, searchCategory);
        } else {
            foods = shopService.searchFoods(query, null);
        }

        // foods 리스트가 null인지 체크하고 빈 리스트로 초기화
        if (foods == null) {
            foods = List.of(); // Java 11 이상에서는 List.of() 사용
        }

        // 모든 카테고리 조회
        List<CategoryDto> categories = categoryService.findAllCategories();

        // 카테고리별 추천 상품 조회
        Map<Integer, List<FoodDto>> categoryRecommendations = categories.stream()
                .collect(Collectors.toMap(
                        CategoryDto::getCategory_id,
                        category -> shopService.findTopNByCategoryId(category.getCategory_id(), 5)
                ));

        model.addAttribute("foods", foods);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryRecommendations", categoryRecommendations);

        return "shop";
    }
}
