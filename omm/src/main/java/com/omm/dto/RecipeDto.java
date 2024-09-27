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

    
}
