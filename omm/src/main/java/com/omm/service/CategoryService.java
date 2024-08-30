package com.omm.service;

import com.omm.dao.CategoryDao;
import com.omm.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;


    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CategoryDto saveCategory(String category_name, Integer parent_category_id) {
        Optional<CategoryDto> existingCategory = categoryDao.findCategoryByName(category_name);
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        } else {
            CategoryDto category = new CategoryDto();
            category.setCategory_name(category_name);
            category.setParent_category_id(parent_category_id);
            categoryDao.insertCategory(category);
            return category;
        }
    }

    public Optional<CategoryDto> findCategoryByName(String category_name) {
        return categoryDao.findCategoryByName(category_name);
    }
}
