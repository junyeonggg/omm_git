package com.omm.dao;

import com.omm.dto.FoodDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopDao {

    @Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId FROM tbl_food WHERE category_id = #{categoryId}")
    List<FoodDto> findFoodsByCategory(@Param("categoryId") int categoryId);

    @Select("<script>" +
            "SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId " +
            "FROM tbl_food " +
            "WHERE category_id IN (" +
            "SELECT category_id FROM tbl_category " +
            "WHERE category_id = #{categoryId} OR parent_category_id = #{categoryId}" +
            ")" +
            "</script>")
    List<FoodDto> findFoodsByCategoryWithSubCategories(@Param("categoryId") int categoryId);

    @Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId FROM tbl_food WHERE food_name LIKE CONCAT('%', #{query}, '%')")
    List<FoodDto> searchFoods(@Param("query") String query);

    @Select("SELECT * FROM tbl_food WHERE food_product_id = #{foodProductId}")
    FoodDto getFoodById(@Param("foodProductId") String foodProductId);
}