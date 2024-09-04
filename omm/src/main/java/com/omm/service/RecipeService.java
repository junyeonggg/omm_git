package com.omm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.RecipeDao;
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
		return recipeDao.selectRecipeByPagingSearch(startRecord,recordSize,keyword);
	}


	public void insertIngre(Recipe_ingre ingre) {
		recipeDao.insertIngre(ingre);
	}


	public int findRecipeByMangeId(String mange_id) {
		return recipeDao.findRecipeByMangeId(mange_id);
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

}
