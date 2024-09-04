package com.omm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.omm.dto.CookingSequenceDto;
import com.omm.dto.PagingSearch;
import com.omm.dto.RecipeDto;
import com.omm.service.RecipeService;

@Controller
public class RecipeController {
	@Autowired
	private RecipeService recipeService;

	// 레시피를 db에 넣기 위한 메서드
//	@GetMapping("/insert_recipe")
//	public String insert_recipe() {
//		try {
////			String path = "C:\\Users\\admin\\Desktop\\city";
//			String path = "C:\\Users\\admin\\Desktop\\recipe.csv";
//			File recipe_csv = new File(path);
//			// 입력 스트림
//			FileReader recipe_list = new FileReader(recipe_csv);
//			BufferedReader bfReader = new BufferedReader(recipe_list);
//			String line = "";
//			int count = 0;
//			while ((line = bfReader.readLine()) != null) {
//				RecipeDto recipe = new RecipeDto();
//				line = line.replace(",", "-,-");
//				String[] data_list = line.split(",");
//				if (data_list.length != 13) {
////					System.out.println("length : " + data_list.length);
//					for(String d : data_list) {
////						System.out.print(d+" ");
////						System.out.println("line : "+line);
//					}
////					System.out.println();
//
//				}
//				recipe.setMange_id(data_list[1]);
//				recipe.setRecipe_title(data_list[2]);
//				recipe.setRecipe_food_name(data_list[3]);
//				recipe.setUser_id(data_list[4]);
//				recipe.setRecipe_method(data_list[5]);
//				recipe.setRecipe_status(data_list[6]);
//				recipe.setRecipe_ingredient(data_list[7]);
//				recipe.setRecipe_serving(data_list[9]);
//				recipe.setRecipe_level(data_list[10]);
//				recipe.setRecipe_time(data_list[11]);
//				recipe.setRecipe_describe(data_list[12]);
//				recipe = recipe.reRe(recipe);
//				
//				recipeService.insertRecipe(recipe);
//				System.out.println(recipe.toString());
//				
//
//				// 첫번째 행을 가져오지 않기 위한 코드
//			}
//		} catch (Exception e) {
//			System.out.println();
//			e.printStackTrace();
//		}
//
//		return "recipe_list";
//	}

	// 레시피 재료를 넣기위한 메서드
//	@GetMapping("/insert_ingre2")
//	public String insert_ingre() {
//		try {
//			String path = "C:\\Users\\admin\\Desktop\\recipe_ingre.csv";
//			File recipe_csv = new File(path);
//			// 입력 스트림
//			FileReader recipe_list = new FileReader(recipe_csv);
//			BufferedReader bfReader = new BufferedReader(recipe_list);
//			String line = "";
//			int count = 0;
//			while ((line = bfReader.readLine()) != null) {
//				RecipeDto recipe = new RecipeDto();
//				if (line.startsWith(",")) {
//					continue;
//				}
//				System.out.println(line);
//				String[] data_list = line.split(",");
//				String[] ingre_list = data_list[2].replace("|",",").split(",");
//				String temp_type = ""; 
//				
//
//				// 첫번째 행을 가져오지 않기 위한 코드
//			}
//		} catch (Exception e) {
//			System.out.println();
//			e.printStackTrace();
//		}
//
//		return "recipe_list";
//	}

	// 레시피 step db에 넣기
	@GetMapping("/insert_step")
	public String insert_step() {
		try {
		String path = "C:\\Users\\admin\\Desktop\\recipe_sequence.csv";
		File recipe_csv = new File(path);
		// 입력 스트림
		FileReader recipe_list = new FileReader(recipe_csv);
		BufferedReader bfReader = new BufferedReader(recipe_list);
		String line = "";
		int count = 0;
		while ((line = bfReader.readLine()) != null) {
			if (line.startsWith(",")) {
				continue;
			}
//			String[] line_list = line.split(",");
			String[] line_list = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
			int cnt = 0;
//			for(String i : line_list) {
//				
//				System.out.println("cnt "+cnt+" : "+i);
//				cnt++;
//			}
			CookingSequenceDto sequence = new CookingSequenceDto();
			
			// 레시피 id는 mange_id를 이용해서 가져온다.
//			System.out.println(line);
			int recipe_id = recipeService.findRecipeByMangeId(line_list[1]); 
			sequence.setRecipe_id(recipe_id);
			sequence.setSequence_text(line_list[2]);
			sequence.setSequence_step_no(Integer.valueOf(line_list[3]));
			recipeService.insertRecipeSequence(sequence);
			if(line.equals("30206,1751398,버터를 바른틀에 반죽을 80~90%정도 채워지게 붓습니다. 아몬드 슬라이스를 넣은후 180도씨 예열오븐에 25~30분간 굽습니다.오븐,4")) {
				System.out.println("끝");
				break;
			}
		}
	} catch (Exception e) {
		System.out.println();
		e.printStackTrace();
	}
		return "redirect:/";
	}

	@GetMapping("/recipe_list")
	public String recipe_list_page(@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page_no, Model model) {
		int recipe_list_size = recipeService.selectAll(keyword);
		PagingSearch pagingSearch = new PagingSearch(recipe_list_size, page_no);
		pagingSearch.setKeyword(keyword);
		List<RecipeDto> recipe_list = recipeService.selectRecipeByPagingSearch(pagingSearch.getStartRecord(),
				pagingSearch.getRecordSize(), pagingSearch.getKeyword());
//		System.out.println(pagingSearch.toString());
		model.addAttribute("paging", pagingSearch);
		model.addAttribute("recipe_list", recipe_list);
		return "recipe_list";
	}
	// 가장 최근 수정 2024-09-04

	@GetMapping("/recipe_list/{recipe_id}")
	public String recipe_detail(@PathVariable("recipe_id") int recipe_id,Model model) {
		RecipeDto recipe = recipeService.findRecipeByRecipe_id(recipe_id);
		model.addAttribute("recipe", recipe);
		List<CookingSequenceDto> recipe_sequence = recipeService.selectRecipeSequenceByRecipeId(recipe_id);
		recipe_sequence.forEach(d->System.out.println(d));
		
		model.addAttribute("recipe_sequence", recipe_sequence);
		return "recipe";
	}
	
}
