package com.omm.dao;

import com.omm.dto.FoodDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopDao {

    @Select("SELECT * FROM tbl_food WHERE category_id = #{categoryId} ORDER BY food_lprice LIMIT #{limit}")
    List<FoodDto> findTopNByCategoryId(@Param("categoryId") int categoryId, @Param("limit") int limit);

    @Select("<script>" +
            "SELECT * FROM tbl_food WHERE food_name LIKE CONCAT('%', #{query}, '%')" +
            "<if test='searchCategory != null'>" +
            " AND search_category = #{searchCategory}" +
            "</if>" +
            "</script>")
    List<FoodDto> searchFoodsByCategory(@Param("query") String query, @Param("searchCategory") String searchCategory);

    @Select("SELECT * FROM tbl_food WHERE food_name LIKE CONCAT('%', #{query}, '%')")
    List<FoodDto> searchFoods(@Param("query") String query);
}