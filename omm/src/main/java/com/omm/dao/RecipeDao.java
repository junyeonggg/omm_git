package com.omm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.omm.dto.CommentDto;
import com.omm.dto.CookingSequenceDto;
import com.omm.dto.ImgDto;
import com.omm.dto.PagingSearch;
import com.omm.dto.RecipeDto;
import com.omm.dto.Recipe_ingre;
import org.springframework.dao.DataAccessException;

import javax.xml.crypto.Data;

@Mapper
public interface RecipeDao {

	@Insert("insert into tbl_recipe values(null,#{recipe_title},#{recipe_food_name},#{user_id},0,0,#{recipe_method},#{recipe_status},#{recipe_ingredient},#{recipe_serving},#{recipe_level},#{recipe_time},#{recipe_describe},#{recipe_create_date},#{mange_id})")
	void insertRecipe(RecipeDto recipe);

//	@Select("select count(*) from tbl_recipe where recipe_food_name like concat('%',#{keyword},'%') ${query};")
	@Select("select count(*) from tbl_recipe where recipe_food_name like concat('%',#{keyword},'%') and recipe_method like concat('%',#{method},'%') and recipe_status like concat('%',#{status},'%') and recipe_ingredient like concat('%',#{ingre},'%')")
	int selectAll(@Param("keyword")String keyword,@Param("method") String method,@Param("status") String status, @Param("ingre")String ingre);

	@Select("select recipe_id,recipe_title,recipe_food_name,user_nickname as user_id, recipe_view,recipe_recommend_cnt,recipe_method,recipe_status,recipe_ingredient,recipe_serving,recipe_time,recipe_level,recipe_describe,recipe_create_date from tbl_recipe join tbl_member on tbl_member.user_id = tbl_recipe.user_id where recipe_food_name like concat('%',#{keyword},'%') and recipe_method like concat('%',#{method},'%') and recipe_status like concat('%',#{status},'%') and recipe_ingredient like concat('%',#{ingre},'%')  order by recipe_id desc limit #{startRecord},#{recordSize};")
	List<RecipeDto> selectRecipeByPagingSearch(PagingSearch paging);

	@Insert("insert into tbl_ingre values(null,#{ingre_type},#{ingre_name},#{ingre_info},#{recipe_id})")
	void insertIngre(Recipe_ingre ingre);

	@Select("select recipe_id from tbl_recipe where mange_id=#{mange_id}")
	int findRecipeIdByMangeId(String mange_id);

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

	
	@Select("select * from tbl_ingre where recipe_id=#{recipe_id}")
	List<Recipe_ingre> selectRecipeIngreByRecipeId(int recipe_id);

	@Select("select distinct ingre_type from tbl_ingre where recipe_id = #{recipe_id}")
	List<String> selectRecipeIngreTypeByRecipeId(int recipe_id);

	// 조회수, 추천수 증가
	@Update("update ${table_name} set ${column_name} = ${column_name}+1 where ${target_column}=${target_id};")
	boolean increView(@Param("table_name") String table_name,@Param("column_name") String column_name, @Param("target_column") String target_column, @Param("target_id") String target_id);

	// 추천수 감소
	@Update("update ${table_name} set ${column_name} = ${column_name}-1 where ${target_column}=${target_id};")
	boolean decreView(@Param("table_name") String table_name,@Param("column_name") String column_name, @Param("target_column") String target_column, @Param("target_id") String target_id);
	
	//찜 추가
	@Insert("insert into tbl_like values(null,#{user_id},#{reference_type},#{target_id})")
	void likeSet(@Param("user_id") String user_id,@Param("reference_type") int reference_type,@Param("target_id") String target_id);

	// 찜 삭제
	@Delete("delete from tbl_like where user_id=#{user_id} and reference_type=#{reference_type} and target_id=#{target_id}")
	void likeUnSet(@Param("user_id")String user_id,@Param("reference_type") int reference_type,@Param("target_id") String target_id);

	@Select("select count(*) from tbl_like where user_id=#{user_id} and reference_type=#{reference_type} and target_id=#{target_id}")
	int getLikeStatus(@Param("user_id")String user_id,@Param("reference_type") int reference_type,@Param("target_id") int target_id);

	@Select("select distinct recipe_method from tbl_recipe;")
	List<String> getMethod();

	@Select("select distinct recipe_status from tbl_recipe")
	List<String> getStatus();

	@Select("select distinct recipe_ingredient from tbl_recipe")
	List<String> getIngre();

	@Select("select recipe_id from tbl_recipe where user_id=#{user_id} order by recipe_id desc limit 0,1;")
	int getInsertRecipeId(String user_id);

	@Select("SELECT sequence_id FROM tbl_cooking_sequence where recipe_id = #{recipe_id} order by sequence_step_no desc limit 0,1;")
	int getTargetId(CookingSequenceDto cookingSequenceDto);

	@Insert("insert into tbl_img values(null,#{img_name},#{img_org_name},#{img_path},#{target_id},#{reference_type})")
	void insertImg(ImgDto imgDto);

	@Select("select img_id from tbl_img where img_name=#{img_name}")
	int getImgId(ImgDto imgDto);

	@Update("update tbl_cooking_sequence set img_id=#{img_id} where sequence_id=#{target_id}")
	void updateImgIdAtSequence(@Param("target_id")int target_id,@Param("img_id")int img_id);

	@Select("select * from tbl_img where img_id=#{img_id}")
	ImgDto getImgByImgId(int img_id);
	
	@Update("update tbl_comment set comment_content=#{newCommentText} where comment_id=#{commentId}")
	void updateComment(@Param("newCommentText") String newCommentText,
					   @Param("commentId") int commentId) throws DataAccessException;
	@Delete("delete from tbl_comment where comment_id=#{commentId}")
	void deleteComment(@Param("commentId") int commentId) throws DataAccessException;
	@Select("select ingre_type, ingre_name from tbl_ingre join tbl_recipe on tbl_recipe.recipe_id = tbl_ingre.recipe_id")
	List<String> joinIngreType() throws DataAccessException;

	@Delete("delete from tbl_recipe where recipe_id=#{recipe_id}")
	void deleteRecipe(@Param("recipe_id") int recipe_id) throws DataAccessException;


	@Select("select * from tbl_ingre where ingre_id=#{ingre_id}")
	Recipe_ingre selectIngreByIngreId(int ingre_id);

	@Update("update tbl_ingre set ingre_name=#{ingre_name}, ingre_info =#{ingre_info} where ingre_id=#{ingre_id}")
	void updateIngre(Recipe_ingre db_ingre);

	@Select("SELECT recipe_id, recipe_title, recipe_food_name, tbl_recipe.user_id, recipe_view, recipe_recommend_cnt, recipe_method, recipe_status, recipe_ingredient, recipe_serving, recipe_level, recipe_time, recipe_describe, recipe_create_date, mange_id FROM omm.tbl_like join tbl_recipe on tbl_recipe.recipe_id = tbl_like.target_id where reference_type=1  and tbl_like.user_id=#{user_id} and recipe_food_name like concat('%',#{pagingSearch.keyword},'%') order by recipe_id desc limit #{pagingSearch.startRecord},#{pagingSearch.recordSize};")
	List<RecipeDto> selectLikeRecipe(@Param("user_id")String user_id,@Param("pagingSearch") PagingSearch pagingSearch);

	@Select("SELECT count(*) FROM omm.tbl_like join tbl_recipe on tbl_recipe.recipe_id = tbl_like.target_id where reference_type=1  and tbl_like.user_id=#{user_id} and recipe_food_name like concat('%',#{keyword},'%');")
	int selectLikeRecipeAll(@Param("keyword")String keyword, @Param("user_id")String user_id);

	@Select("SELECT cs.sequence_text, cs.sequence_step_no FROM tbl_cooking_sequence cs JOIN tbl_recipe r ON cs.recipe_id = r.recipe_id WHERE r.recipe_id = #{recipe_id} ORDER BY cs.sequence_step_no;\n")
	List<CookingSequenceDto> selectRecipeSequenceAndTextByRecipeId(@Param("recipe_id") int recipe_id);

	@Update("update tbl_recipe set recipe_title=#{recipe_title}, recipe_food_name=#{recipe_food_name}, recipe_method=#{recipe_method}, recipe_status=#{recipe_status}, recipe_ingredient=#{recipe_ingredient}, recipe_serving=#{recipe_serving}, recipe_level=#{recipe_level}, recipe_time=#{recipe_time}, recipe_describe=#{recipe_describe} where recipe_id = #{recipe_id}")
	int updateRecipeByRecipeId(RecipeDto recipeDto);

	@Select("select * from tbl_recipe where recipe_id=#{recipe_id}")
	RecipeDto selectRecipeById(int recipe_id);

	@Delete("delete from tbl_ingre where recipe_id =#{recipe_id}")
	void deleteIngreByRecipeId(int recipe_id);


	@Delete("delete from tbl_cooking_sequence where recipe_id = #{recipe_id}")
	void deleteSequenceImgByRecipeId(int recipe_id);
}
