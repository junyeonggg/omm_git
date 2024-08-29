package com.omm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecipeController {
	
	@GetMapping("/recipe_list")
	public String recipe_list_page() {
		return "recipe_list";
	}
}
