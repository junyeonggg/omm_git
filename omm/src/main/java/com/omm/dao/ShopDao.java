package com.omm.dao;

import com.omm.dto.FoodDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopDao {

    @Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, " +
            "food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId " +
            "FROM tbl_food " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<FoodDto> findAll(@Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId " +
            "FROM tbl_food " +
            "WHERE category_id = #{categoryId} " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<FoodDto> findFoodsByCategory(@Param("categoryId") int categoryId, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("<script>" +
            "SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId " +
            "FROM tbl_food " +
            "WHERE category_id IN (" +
            "SELECT category_id FROM tbl_category " +
            "WHERE category_id = #{categoryId} OR parent_category_id = #{categoryId}" +
            ") " +
            "LIMIT #{pageSize} OFFSET #{offset}" +
            "</script>")
    List<FoodDto> findFoodsByCategoryWithSubCategories(@Param("categoryId") int categoryId, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId " +
            "FROM tbl_food " +
            "WHERE food_name LIKE CONCAT('%', #{query}, '%') " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<FoodDto> searchFoods(@Param("query") String query, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM tbl_food WHERE food_name LIKE CONCAT('%', #{query}, '%')")
    int countFoodsByQuery(@Param("query") String query);

    @Select("SELECT COUNT(*) FROM tbl_food WHERE category_id IN (" +
            "SELECT category_id FROM tbl_category WHERE category_id = #{categoryId} OR parent_category_id = #{categoryId}" +
            ")")
    int countFoodsByCategory(@Param("categoryId") int categoryId);

    @Select("SELECT COUNT(*) FROM tbl_food")
    int countAllFoods();

    @Select("SELECT * FROM tbl_food WHERE food_product_id = #{foodProductId}")
    @Results({
            @Result(property = "foodProductId", column = "food_product_id"),
            @Result(property = "foodName", column = "food_name"),
            @Result(property = "foodLprice", column = "food_lprice"),
            @Result(property = "foodImg", column = "food_img")
    })
    FoodDto getFoodById(@Param("foodProductId") String foodProductId);
}