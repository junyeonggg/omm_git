package com.omm.dao;

import com.omm.dto.CategoryDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface CategoryDao {

    @Insert("INSERT INTO tbl_category (category_name, parent_category_id) VALUES (#{category_name}, #{parent_category_id})")
    @Options(useGeneratedKeys = true, keyProperty = "category_id")
    void insertCategory(CategoryDto category);

    @Select("SELECT category_id, category_name, parent_category_id FROM tbl_category WHERE category_name = #{category_name}")
    Optional<CategoryDto> findCategoryByName(String category_name);
}
