package com.omm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.RecipeDao;
import com.omm.dto.CommentDto;
import com.omm.dto.CookingSequenceDto;
import com.omm.dto.RecipeDto;
import com.omm.dto.Recipe_ingre;

@Service
public class RecipeService {
	@Autowired
	private RecipeDao recipeDao;

	public void insertRecipe(RecipeDto recipe) {
		recipeDao.insertRecipe(recipe);
	}

	public int selectAll(String keyword) {
		return recipeDao.selectAll(keyword);
	}

	public List<RecipeDto> selectRecipeByPagingSearch(int startRecord, int recordSize, String keyword) {
		return recipeDao.selectRecipeByPagingSearch(startRecord, recordSize, keyword);
	}

	public void insertIngre(Recipe_ingre ingre) {
		recipeDao.insertIngre(ingre);
	}

	public int findRecipeIdByMangeId(String mange_id) {
		return recipeDao.findRecipeIdByMangeId(mange_id);
	}

	public void insertRecipeSequence(CookingSequenceDto sequence) {
		recipeDao.insertRecipeSequence(sequence);
	}

	public RecipeDto findRecipeByRecipe_id(int recipe_id) {

		return recipeDao.findRecipeByRecipe_id(recipe_id);
	}

	public List<CookingSequenceDto> selectRecipeSequenceByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeSequenceByRecipeId(recipe_id);
	}

	public String getUserNicknameByUserId(String user_id) {
		return recipeDao.getUserNicknameByUserId(user_id);
	}

	public List<CommentDto> getCommentsByTargetIdAndRefType(int recipe_id, int type_no) {
		return recipeDao.getCommentsByTargetIdAndRefType(recipe_id, type_no);
	}

	public List<Recipe_ingre> selectRecipeIngreByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeIngreByRecipeId(recipe_id);
	}

	public List<String> selectRecipeIngreTypeByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeIngreTypeByRecipeId(recipe_id);
	}

	
	
	public boolean increView(String table_name, String column_name, String target_column, String target_id) {
		return recipeDao.increView(table_name, column_name, target_column, target_id);
	}

	public void likeSet(String user_id, int reference_type, String target_id) {
		recipeDao.likeSet(user_id, reference_type, target_id);
	}

	public void likeUnSet(String user_id, int reference_type, String target_id) {
		recipeDao.likeUnSet(user_id, reference_type, target_id);
	}

	public boolean getLikeStatus(String user_id, int reference_type, int target_id) {
		int cnt = recipeDao.getLikeStatus(user_id, reference_type, target_id);
		if (cnt > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void decreView(String table_name, String column_name, String target_column, String target_id) {
		recipeDao.decreView(table_name, column_name, target_column, target_id);
	}

}
