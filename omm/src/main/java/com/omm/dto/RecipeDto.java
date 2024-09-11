package com.omm.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class RecipeDto {
	public RecipeDto() {
		LocalDate now = LocalDate.now();
		this.recipe_create_date = now.toString();
	}
	
    private int recipe_id;
    private String recipe_title;
    private String recipe_food_name;
    private String user_id;
    private int recipe_view;
    private int recipe_recommend_cnt;
    private String recipe_method;
    private String recipe_status;
    private String recipe_ingredient;
    private String recipe_serving;
    private String recipe_level;
    private String recipe_time;
    private String recipe_describe;
    private String recipe_create_date;
    private String mange_id;
    
    public RecipeDto reRe(RecipeDto recipe) {
    	recipe.setRecipe_title(recipe.getRecipe_title().replace("-", ""));
    	recipe.setRecipe_food_name(recipe.getRecipe_food_name().replace("-",""));
		recipe.setUser_id(recipe.getUser_id().replace("-", ""));
		recipe.setRecipe_method(recipe.getRecipe_method().replace("-",""));
		recipe.setRecipe_status(recipe.getRecipe_status().replace("-",""));
		recipe.setRecipe_ingredient(recipe.getRecipe_ingredient().replace("-",""));
		recipe.setRecipe_serving(recipe.getRecipe_serving().replace("-",""));
		recipe.setRecipe_level(recipe.getRecipe_level().replace("-",""));
		recipe.setRecipe_time(recipe.getRecipe_time().replace("-",""));
		recipe.setRecipe_describe(recipe.getRecipe_describe().replace("-",""));    	
		recipe.setMange_id(recipe.getMange_id().replace("-",""));
    	return recipe;
    }

    
}
