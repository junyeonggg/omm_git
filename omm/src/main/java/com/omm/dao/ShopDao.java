package com.omm.dao;

import com.omm.dto.FoodDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopDao {
    @Insert("INSERT INTO tbl_food (food_id,food_name, food_link, food_img, food_lprice, food_hprice, food_mall_name, food_product_id, food_brand_name, category_id) " +
            "VALUES (null,#{foodTitle}, #{foodLink}, #{foodImage}, #{foodLprice}, #{foodHprice}, #{foodMallName}, #{foodProductId}, #{foodBrand}, #{categoryId})")
    @Options(useGeneratedKeys = true, keyProperty = "foodId")
    void insertFood(FoodDto food);

    @Insert({
            "<script>",
            "INSERT INTO tbl_food (food_id,food_name, food_link, food_img, food_lprice, food_hprice, food_mall_name, food_product_id, food_brand_name, category_id) VALUES ",
            "<foreach collection='foods' item='food' separator=','>",
            "(null,#{food.foodTitle}, #{food.foodLink}, #{food.foodImage}, #{food.foodLprice}, #{food.foodHprice}, #{food.foodMallName}, #{food.foodProductId}, #{food.foodBrand}, #{food.categoryId})",
            "</foreach>",
            "</script>"
    })
    void insertFoods(@Param("foods") List<FoodDto> foods);
}
