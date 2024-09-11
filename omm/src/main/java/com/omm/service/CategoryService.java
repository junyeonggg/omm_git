package com.omm.service;

import com.omm.dao.CategoryDao;
import com.omm.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    // 모든 카테고리를 계층 구조로 조회하는 메서드
    public List<CategoryDto> findAllCategories() {
        List<CategoryDto> categories = categoryDao.findAllCategories();

        // 계층 구조를 위한 맵
        Map<Integer, CategoryDto> categoryMap = new HashMap<>();
        List<CategoryDto> rootCategories = new ArrayList<>();

        for (CategoryDto category : categories) {
            categoryMap.put(category.getCategoryId(), category);
            if (category.getParentCategoryId() == null) {
                rootCategories.add(category);
            } else {
                CategoryDto parentCategory = categoryMap.get(category.getParentCategoryId());
                if (parentCategory != null) {
                    if (parentCategory.getChildren() == null) {
                        parentCategory.setChildren(new ArrayList<>());
                    }
                    parentCategory.getChildren().add(category);
                }
            }
        }

        return rootCategories;
    }
}
