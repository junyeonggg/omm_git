package com.omm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omm.dao.RecipeDao;
import com.omm.dto.CommentDto;
import com.omm.dto.CookingSequenceDto;
import com.omm.dto.PagingSearch;
import com.omm.dto.RecipeDto;
import com.omm.dto.Recipe_ingre;
import com.omm.service.RecipeService;

@Controller
public class RecipeController {
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private RecipeDao dao;
	@Autowired
	private PasswordEncoder passwordEncoder;
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
//		return "redirect:/";
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
//				Recipe_ingre ingre = new Recipe_ingre();
//				if (line.startsWith(",")) {
//					continue;
//				}
////				System.out.println(line);
//				String[] data_list = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
//				ingre.setIngre_type(data_list[2]);
//				ingre.setIngre_name(data_list[3]);
//				ingre.setIngre_info(data_list[4]);
//				ingre.setRecipe_id(recipeService.findRecipeIdByMangeId(data_list[1]));
//				recipeService.insertIngre(ingre);
//
//				if (data_list[0].equals("45767")) {
//					System.out.println(data_list[0]);
//					break;
//				} else if (line.equals("45767,1751398,[양자택일],인스턴트커피,1큰술")) {
//					System.out.println(line);
//					break;
//				}
//				// 첫번째 행을 가져오지 않기 위한 코드
//			}
//		} catch (Exception e) {
//			System.out.println();
//			e.printStackTrace();
//		}
//
//		return "redirect:/";
//	} // 마지막 수정 2024-09-06


//	 레시피 step db에 넣기
//	@GetMapping("/insert_step")
//	public String insert_step() {
//		try {
//		String path = "C:\\Users\\admin\\Desktop\\recipe_sequence.csv";
//		File recipe_csv = new File(path);
//		// 입력 스트림
//		FileReader recipe_list = new FileReader(recipe_csv);
//		BufferedReader bfReader = new BufferedReader(recipe_list);
//		String line = "";
//		int count = 0;
//		while ((line = bfReader.readLine()) != null) {
//			if (line.startsWith(",")) {
//				continue;
//			}
//			String[] line_list = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);
//			int cnt = 0;
////			for(String i : line_list) {
////
////				System.out.println("cnt "+cnt+" : "+i);
////				cnt++;
////			}
//			CookingSequenceDto sequence = new CookingSequenceDto();
//
//			// 레시피 id는 mange_id를 이용해서 가져온다.
////			System.out.println(line);
//			int recipe_id = recipeService.findRecipeIdByMangeId(line_list[1]); 
//			int recipe_id = recipeService.findRecipeByMangeId(line_list[1]);
//			sequence.setRecipe_id(recipe_id);
//			sequence.setSequence_text(line_list[2]);
//			sequence.setSequence_step_no(Integer.valueOf(line_list[3]));
//			recipeService.insertRecipeSequence(sequence);
//			if(line_list[0].equals("30206")) {
//				System.out.println("끝");
//				break;
//			}
//		}
//	} catch (Exception e) {
//		System.out.println();
//		e.printStackTrace();
//	}
//		return "redirect:/";
//	}

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

	//레시피 작성
	@GetMapping("/recipe/write")
	public String recipe_write() {
		
		return "recipe_write";
	}
	
	
	// 레시피 detail
	@GetMapping("/recipe_list/{recipe_id}")
	public String recipe_detail(@PathVariable("recipe_id") int recipe_id, Model model, Principal principal) {
		
		// 해당 레시피 정보
		RecipeDto recipe = recipeService.findRecipeByRecipe_id(recipe_id);
		model.addAttribute("recipe", recipe);

		// 해당 레시피의 순서
		List<CookingSequenceDto> recipe_sequence = recipeService.selectRecipeSequenceByRecipeId(recipe_id);
		model.addAttribute("recipe_sequence", recipe_sequence);

		// 해당 레시피의 재료
		List<Recipe_ingre> ingre_list = recipeService.selectRecipeIngreByRecipeId(recipe_id);
		// 재료 type_list
		List<String> ingre_type_list = recipeService.selectRecipeIngreTypeByRecipeId(recipe_id);
		model.addAttribute("ingre_type_list", ingre_type_list);
		model.addAttribute("ingre_list", ingre_list);

		// 현재 로그인한 사람 닉네임 가져오기
		String user_nickname = "";
		try {
			user_nickname = recipeService.getUserNicknameByUserId(principal.getName());
//			System.out.println("user_nickname : "+user_nickname);
			model.addAttribute("user_nickname", user_nickname);
			model.addAttribute("user_id", principal.getName());
		} catch (Exception e) {
			model.addAttribute("user_nickname", "Null");
		}

		// 해당 레시피의 댓글
		List<CommentDto> comment_list = recipeService.getCommentsByTargetIdAndRefType(recipe.getRecipe_id(), 1);
		model.addAttribute("comment_list", comment_list);

		// 해당 레시피의 찜 상태
		try {
			boolean checked = recipeService.getLikeStatus(principal.getName(), 1, recipe_id);
			model.addAttribute("like_checked", checked);
		} catch (Exception e) {
			model.addAttribute("like_checked", false);
		}

		return "recipe";
	}

	// 조회수를 증가시킬때 매핑되는 메서드
	// 1. table_name   조회수 변경할 테이블 이름 ex) tbl_recipe
	// 2. column_name  조회수 컬럼 명 ex) recipe_view
	// 3. target_column  where조건 컬럼 명  ex) recipe_id
	// 4. target_id    where 조건 값  ex) 168471
	@ResponseBody
	@PostMapping("/increView")
	public boolean increView(@RequestParam("table_name") String table_name,
			@RequestParam("column_name") String column_name, @RequestParam("target_column") String target_column,
			@RequestParam("target_id") String target_id) {
		boolean flag = false;
		try {
			flag = recipeService.increView(table_name, column_name, target_column, target_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 추천 수를 증가, 또는 감소시키는 메서드
	// 1. reference_type  ex)  1(recipe) 2(food) 3(inquire)
	// 2. target_id   ex) 16874
	// 3. checked     추천 체크박스가 체크 되어 있는지 여부 ex) true, false
	@ResponseBody
	@PostMapping("/likeSet")
	public boolean likeSet(@RequestParam("reference_type") int reference_type,
			@RequestParam("target_id") String target_id, @RequestParam("checked") boolean checked,
			Principal principal) {
		String user_id = "";
		try{
			user_id = principal.getName();
		}catch (Exception e) {
		}
		String table_name = "";
		String column_name = "";
		String target_column = "";
		if (reference_type == 1) {
			table_name = "tbl_recipe";
			column_name = "recipe_recommend_cnt";
			target_column = "recipe_id";
		} else if (reference_type == 2) {
			table_name = "";
			column_name = "";
			target_column = "recipe_id";
		}
		boolean flag = false;
		if (checked & user_id != "") {
			// 체크되어있으면 찜 추가하고 추천수를 증가시킴
			recipeService.likeSet(user_id, reference_type, target_id); // 찜 추가
			recipeService.increView(table_name, column_name, target_column, target_id); // 추천 수 증가
		} else {
			// 체크x 이면 찜 tbl에서 삭제하고 추천수를 감소시킴
			recipeService.likeUnSet(user_id, reference_type, target_id);
			recipeService.decreView(table_name, column_name, target_column, target_id); // 추천 수 감소
		}

		return flag;

	}

	// 임시로 비번 업데이트
//	@GetMapping("/encode_admin")
//	public String asdf() {
//		String encode = passwordEncoder.encode("123123123");
//		dao.asdf(encode);
//		return "redirect:/";
//	}

	
	// 임시로 비번 업데이트
//	@GetMapping("/encode_admin")
//	public String asdf() {
//		String encode = passwordEncoder.encode("123123123");
//		dao.asdf(encode);
//		return "redirect:/";
//	}
	
}
