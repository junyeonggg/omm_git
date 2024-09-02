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

        // 배너 이미지 URL 리스트
        List<String> bannerImages = Arrays.asList(
                "/images/banner1.jpg",
                "/images/banner2.jpg",
                "/images/banner3.jpg"
        );
        model.addAttribute("bannerImages", bannerImages);

        // 검색 결과 초기화
        List<FoodDto> foods;

        if (query != null && !query.trim().isEmpty()) {
            // 검색어가 있는 경우 검색어와 카테고리를 기준으로 검색
            foods = shopService.searchFoods(query, searchCategory);
        } else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
            // 검색어는 없고 카테고리만 있는 경우
            foods = shopService.searchFoods(null, searchCategory);
        } else {
            // 검색어와 카테고리 모두 없는 경우 전체 데이터 조회
            foods = shopService.searchFoods(null, null);
        }

        // foods 리스트가 null인지 체크하고 빈 리스트로 초기화
        if (foods == null) {
            foods = Collections.emptyList(); // Java 8 호환성을 위해 Collections.emptyList() 사용
        }

        // 모든 카테고리 조회
        List<CategoryDto> categories = categoryService.findAllCategories();

        // categories가 null인지 체크하고 빈 리스트로 초기화
        if (categories == null) {
            categories = Collections.emptyList();
        }

        // 카테고리별 추천 상품 조회
        Map<Integer, List<FoodDto>> categoryRecommendations = categories.stream()
                .collect(Collectors.toMap(
                        CategoryDto::getCategory_id,
                        category -> {
                            List<FoodDto> recommendations = shopService.findTopNByCategoryId(category.getCategory_id(), 5);
                            return recommendations != null ? recommendations : Collections.emptyList();
                        }
                ));

        model.addAttribute("foods", foods);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryRecommendations", categoryRecommendations);

        return "shop";
    }
}
