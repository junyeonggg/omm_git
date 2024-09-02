package com.omm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.omm.dto.RecipeDto;

@Mapper
public interface RecipeDao {

	@Insert("insert into tbl_recipe values(null,#{recipe_title},#{recipe_food_name},'admin',null,null,#{recipe_method},#{recipe_status},#{recipe_ingredient},#{recipe_serving},#{recipe_level},#{recipe_time},#{recipe_describe},#{recipe_create_date},#{mange_id})")
	void insertRecipe(RecipeDto recipe);

	@Select("select count(*) from tbl_recipe")
	int selectAll();

	@Select("select * from tbl_recipe where recipe_food_name like concat('%',#{keyword},'%') limit #{start_record},#{record_size};")
	List<RecipeDto> selectRecipeByPagingSearch(@Param("start_record") int start_record, @Param("record_size") int record_size,@Param("keyword") String keyword);

}
