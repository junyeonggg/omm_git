package com.omm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.omm.dto.CookingSequenceDto;
import com.omm.dto.RecipeDto;
import com.omm.dto.Recipe_ingre;

@Mapper
public interface RecipeDao {

	@Insert("insert into tbl_recipe values(null,#{recipe_title},#{recipe_food_name},'admin',null,null,#{recipe_method},#{recipe_status},#{recipe_ingredient},#{recipe_serving},#{recipe_level},#{recipe_time},#{recipe_describe},#{recipe_create_date},#{mange_id})")
	void insertRecipe(RecipeDto recipe);

	@Select("select count(*) from tbl_recipe where recipe_food_name like concat('%',#{keyword},'%');")
	int selectAll(String keyword);

	@Select("select * from tbl_recipe where recipe_food_name like concat('%',#{keyword},'%') order by recipe_id desc limit #{start_record},#{record_size};")
	List<RecipeDto> selectRecipeByPagingSearch(@Param("start_record") int start_record, @Param("record_size") int record_size,@Param("keyword") String keyword);

	@Insert("insert into tbl_ingre values(null,#{ingre_type},#{ingre_name},#{ingre_info},#{mange_id})")
	void insertIngre(Recipe_ingre ingre);

	@Select("select recipe_id from tbl_recipe where mange_id=#{mange_id}")
	int findRecipeByMangeId(String mange_id);

	@Insert("insert into tbl_cooking_sequence values(null,#{recipe_id},#{sequence_text},null,#{sequence_step_no})")
	void insertRecipeSequence(CookingSequenceDto sequence);

	@Select("select * from tbl_recipe where recipe_id = #{recipe_id}")
	RecipeDto findRecipeByRecipe_id(int recipe_id);

	@Select("select * from tbl_cooking_sequence where recipe_id = #{recipe_id} order by sequence_step_no")
	List<CookingSequenceDto> selectRecipeSequenceByRecipeId(int recipe_id);

}
