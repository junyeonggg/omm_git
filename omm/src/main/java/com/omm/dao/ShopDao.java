package com.omm.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.omm.dto.CartDto;
import com.omm.dto.CommentDto;
import com.omm.dto.FoodDto;
import com.omm.dto.OrderDto;

@Mapper
public interface ShopDao {

	@Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, "
			+ "food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId "
			+ "FROM tbl_food " + "LIMIT #{pageSize} OFFSET #{offset}")
	List<FoodDto> findAll(@Param("pageSize") int pageSize, @Param("offset") int offset);

	@Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId "
			+ "FROM tbl_food " + "WHERE category_id = #{categoryId} " + "LIMIT #{pageSize} OFFSET #{offset}")
	List<FoodDto> findFoodsByCategory(@Param("categoryId") int categoryId, @Param("pageSize") int pageSize,
			@Param("offset") int offset);

	@Select("<script>"
			+ "SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId "
			+ "FROM tbl_food " + "WHERE category_id IN (" + "SELECT category_id FROM tbl_category "
			+ "WHERE category_id = #{categoryId} OR parent_category_id = #{categoryId}" + ") "
			+ "LIMIT #{pageSize} OFFSET #{offset}" + "</script>")
	List<FoodDto> findFoodsByCategoryWithSubCategories(@Param("categoryId") int categoryId,
			@Param("pageSize") int pageSize, @Param("offset") int offset);

	@Select("SELECT food_id AS foodId, food_name AS foodName, food_img AS foodImg, food_lprice AS foodLprice, food_mall_name AS foodMallName, food_product_id AS foodProductId, search_category AS searchCategory, category_id AS categoryId "
			+ "FROM tbl_food " + "WHERE food_name LIKE CONCAT('%', #{query}, '%') "
			+ "LIMIT #{pageSize} OFFSET #{offset}")
	List<FoodDto> searchFoods(@Param("query") String query, @Param("pageSize") int pageSize,
			@Param("offset") int offset);

	@Select("SELECT COUNT(*) FROM tbl_food WHERE food_name LIKE CONCAT('%', #{query}, '%')")
	int countFoodsByQuery(@Param("query") String query);

	@Select("SELECT COUNT(*) FROM tbl_food WHERE category_id IN ("
			+ "SELECT category_id FROM tbl_category WHERE category_id = #{categoryId} OR parent_category_id = #{categoryId}"
			+ ")")
	int countFoodsByCategory(@Param("categoryId") int categoryId);

	@Select("SELECT COUNT(*) FROM tbl_food")
	int countAllFoods();

	@Select("SELECT * FROM tbl_food WHERE food_product_id = #{foodProductId}")
	@Results({ @Result(property = "foodId", column = "food_id"), @Result(property = "foodName", column = "food_name"),
			@Result(property = "foodLprice", column = "food_lprice"),
			@Result(property = "foodImg", column = "food_img") })
	FoodDto getFoodById(@Param("foodProductId") String foodProductId);

	@Insert("INSERT INTO tbl_comment VALUES (null, #{user_id}, #{comment_content}, #{comment_create_date}, #{target_id}, #{parent_comment_id}, #{comment_rating}, #{reference_type});")
	void insertReply(CommentDto comment);

	@Insert("insert into tbl_cart values(null,#{user_id},#{food_id},#{food_quantity},1)")
	void insertCart(CartDto cartDto);

	@Select("select * from tbl_cart join tbl_food on tbl_cart.food_id = tbl_food.food_id where user_id =#{user_id}")
	List<HashMap<String, Object>> getCartByUserId(String user_id);

	@Delete("delete from tbl_cart where cart_id = #{cart_id}")
	void deleteCartByCartId(int cart_id);

	@Select("select count(*) from tbl_cart where user_id=#{user_id} and food_id=#{food_id}")
	int checkCartByUserIdAndFoodId(CartDto cartDto);

	@Update("update tbl_cart set cart_check=${i} where food_id=#{food_id} and user_id=#{name}")
	void changecheck(@Param("i") int i, @Param("food_id") int food_id, @Param("name") String name);

	@Select("select * from tbl_cart join tbl_food on tbl_cart.food_id = tbl_food.food_id where user_id =#{user_id} and cart_check=1")
	List<HashMap<String, Object>> getCartByUserIdAndCheck(String name);

	@Insert("insert into tbl_order values(null,#{order_id}, #{user_id},#{order_date},#{food_id})")
	void insertOrder(OrderDto orderDto);

	@Select("select * from tbl_food where food_id=#{food_id}")
	List<HashMap<String, Object>> getFoodOrder(int food_id);

	// db업데이트
	@Update("update tbl_cart set food_quantity = #{quantity} where user_id=#{user_id} and food_id=#{food_id}")
	void changeCnt(@Param("quantity")int quantity, @Param("food_id")int food_id, @Param("user_id")String user_id);

}