package com.omm.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omm.dao.RecipeDao;
import com.omm.dto.CommentDto;
import com.omm.dto.CookingSequenceDto;
import com.omm.dto.ImgDto;
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
//		return "redirect:/";
//	}

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

	// 레시피 작성
	@GetMapping("/recipe/write")
	public String recipe_write(Model model, Principal principal) {

		// 요리방법
		List<String> method_list = recipeService.getMethod();
		model.addAttribute("method_list", method_list);

		// 요리 상황
		List<String> status_list = recipeService.getStatus();
		model.addAttribute("status_list", status_list);

		// 메인 재료
		List<String> ingre_list = recipeService.getIngre();
		model.addAttribute("ingre_list", ingre_list);
		return "recipe_write";
	}

	@ResponseBody
	@PostMapping("/recipe/insert")
	public String recipeInsert(RecipeDto recipeDto, @RequestParam("recipe_ingre") String recipeIng,
			@RequestParam("cookingSequenceDto") String cookingSequence,
			@RequestParam Map<String, MultipartFile> sequence_img, Principal principal)
			throws IllegalStateException, IOException {

		// 1. 레시피 저장
		System.out.println("먼저 레시피를 저장합니다.");
		System.out.println("저장할 레시피 : " + recipeDto.toString());
		recipeDto.setUser_id(principal.getName());
		recipeService.insertRecipe(recipeDto);
		System.out.println("레시피 저장 완료");
		// 2. 저장한 레시피 id 가져오기
		int recipe_id = recipeService.getInsertRecipeId(principal.getName());
		System.out.println("저장한 레시피의 id를 가져옵니다.");
		System.out.println("id : " + recipe_id);
		// Parsing JSON data
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Recipe_ingre> recipe_ingre = mapper.readValue(recipeIng, new TypeReference<List<Recipe_ingre>>() {
			});
			recipe_ingre.forEach(d -> {
				d.setRecipe_id(recipe_id);
				System.out.println("위에서 가져온 id를 ingre에 저장하여 db에 넣는다.");
				System.out.println("넣을 ingre : " + d.toString());
				recipeService.insertIngre(d);
				System.out.println("저장 완");
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<CookingSequenceDto> cookingSequenceDtos = null;
		try {
			cookingSequenceDtos = mapper.readValue(cookingSequence, new TypeReference<List<CookingSequenceDto>>() {
			});
//			cookingSequenceDtos.forEach(d -> System.out.println(d.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("레시피 조리 순서 저장을 시작합니다.");
		for (int i = 0; i < cookingSequenceDtos.size(); i++) {
			cookingSequenceDtos.get(i).setRecipe_id(recipe_id);
			System.out.println("레시피 id를 먼저 set한다.");
			System.out.println("결과 : " + cookingSequenceDtos.get(i).toString());
			recipeService.insertRecipeSequence(cookingSequenceDtos.get(i));
			System.out.println("db에 저장 완료");
			int target_id = recipeService.getTargetId(cookingSequenceDtos.get(i));
			System.out.println("방금 저장한 시퀀스의 id를 가져온다.");
			System.out.println(sequence_img.get("sequence_img_" + i));
			if (!(sequence_img.get("sequence_img_" + i).getOriginalFilename().equals("blob"))) {
				System.out.println("이미지가 있다. 작업을 시작한다.");
				MultipartFile img = sequence_img.get("sequence_img_" + i);
				String path = "C:\\TeamProject\\name\\omm_git\\omm\\src\\main\\resources\\static\\img\\";
				ImgDto imgDto = new ImgDto();
				String uuid = UUID.randomUUID().toString().substring(0, 8);
				imgDto.setImg_name(uuid + "_" + img.getOriginalFilename());
				imgDto.setImg_path(path);
				imgDto.setReference_type(4);
				imgDto.setTarget_id(target_id);
				imgDto.setImg_org_name(img.getOriginalFilename());
				System.out.println("이미지 DTO : " + imgDto.toString());
				img.transferTo(new File(imgDto.getImg_path() + imgDto.getImg_name()));
				recipeService.insertImg(imgDto);
				System.out.println("이미지 저장 완료..");

				int img_id = recipeService.getImgId(imgDto);
				System.out.println("저장한 이미지의 id를 가져옴 : " + img_id);
				recipeService.updateImgIdAtSequence(target_id, img_id);
				System.out.println("시퀀스 테이블의 img_id를 set 했다.");
			}
			System.out.println(sequence_img.get("sequence_img_" + i));
		}
		// Handle images
		return "Files processed successfully";
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

		return "recipe";
	}

	@GetMapping("/img")
	public ResponseEntity<Resource> downloadImg(@RequestParam("img_id") int img_id) {
		try {
			ImgDto img = recipeService.getImg(img_id);
			Path menuImgPath = Paths.get(img.getImg_path()).resolve(img.getImg_name()).normalize();

			Resource resource = new UrlResource(menuImgPath.toUri());
			if (resource.exists()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + URLEncoder.encode(img.getImg_org_name(), "UTF-8") + "\"")
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
		}
		return null;
	}

	// 임시로 비번 업데이트
//	@GetMapping("/encode_admin")
//	public String asdf() {
//		String encode = passwordEncoder.encode("123123123");
//		dao.asdf(encode);
//		return "redirect:/";
//	}

}
