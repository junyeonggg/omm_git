package com.omm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.omm.dto.CommentDto;
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

	@Select("select user_nickname from tbl_member where user_id = #{user_id}")
	String getUserNicknameByUserId(String user_id);

	@Select("select comment_id,user_nickname as user_id, comment_content, comment_create_date, target_id, parent_comment_id, comment_rating, reference_type from tbl_comment,tbl_member where tbl_comment.user_id = tbl_member.user_id and target_id=#{recipe_id} and reference_type = #{type_no}")
	List<CommentDto> getCommentsByTargetIdAndRefType(@Param("recipe_id") int recipe_id,@Param("type_no") int type_no);

//	@Update("update tbl_member set user_pw=#{encode} where user_id='admin'")
//	void asdf(String encode);

}
