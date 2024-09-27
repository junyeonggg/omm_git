// 준영 수정


// 2024-09-13 금 16:11

// 2024-09-20 금
package com.omm.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import org.springframework.web.bind.annotation.RequestBody;
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
//		String path = "C:\\Users\\admin\\Desktop\\recipe.csv";
//		File recipe_csv = new File(path);
//		int cnt = 0;
//		try (BufferedReader bfReader = new BufferedReader(new FileReader(recipe_csv))) {
//			String line;
//
//			while ((line = bfReader.readLine()) != null) {
//				if(cnt == 184990){
//					break;
//				}
//				String[] lineItems = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
//
//				// 데이터 유효성 검사
//				if (lineItems.length < 13) {
//					System.out.println("Invalid data line: " + line);
//					continue; // 데이터가 부족한 경우 건너뜁니다.
//				}
//
//				RecipeDto recipe = new RecipeDto();
//				recipe.setMange_id(lineItems[1]);
//				recipe.setRecipe_title(lineItems[2]);
//				recipe.setRecipe_food_name(lineItems[3]);
//				recipe.setUser_id("admin");
//				recipe.setRecipe_method(lineItems[5]);
//				recipe.setRecipe_status(lineItems[6]);
//				recipe.setRecipe_ingredient(lineItems[7]);
//				recipe.setRecipe_serving(lineItems[9]);
//				recipe.setRecipe_level(lineItems[10]);
//				recipe.setRecipe_time(lineItems[11]);
//				recipe.setRecipe_describe(lineItems[12]);
//
//				recipeService.insertRecipe(recipe);
//				cnt++;
//				System.out.println("cnt : "+cnt);
//			}
//		} catch (IOException e) {
//			e.printStackTrace(); // IOException만 잡아 처리
//		}
//
//		return "recipe_list";
//	}


	// 레시피 재료를 넣기위한 메서드 수정 : 2024-09-12
//	@GetMapping("/insert_ingre")
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
////				RecipeDto recipe = new RecipeDto();
//				Recipe_ingre ingre = new Recipe_ingre();
//				if (line.startsWith(",")) {
//					continue;
//				}
//				System.out.println(line);
//				String[] line_list = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
//				try{
//					ingre.setIngre_info(line_list[4]);
//				}catch(Exception e){
//
//				}
//				ingre.setIngre_name(line_list[3]);
//				ingre.setIngre_type(line_list[2]);
//				ingre.setRecipe_id(Integer.parseInt(line_list[1]));
//				recipeService.insertIngre(ingre);
//
//				if(line.equals("45767,1751398,[양자택일],인스턴트커피,1큰술") | line_list[0].equals("45767")) {
//					break;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "redirect:/";
//	}
	// 레시피 sequence db에 넣기 수정 : 2024-09-12
	@GetMapping("/insert_step")
	public String insert_step() {
		String path = "C:\\Users\\admin\\Desktop\\recipe_sequence.csv";
		File recipe_csv = new File(path);

		try (BufferedReader bfReader = new BufferedReader(new FileReader(recipe_csv))) {
			String line;
			while ((line = bfReader.readLine()) != null) {
				// Skip lines that start with a comma
				if (line.startsWith(",")) {
					System.out.println("건너뜀");
					continue;
				}

				String[] line_list = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				System.out.println(line);
				// Check if line_list has the correct number of elements
				if (line_list.length < 4) {
					System.out.println("Skipping invalid line: " + line);
					continue;
				}

				// Create and populate CookingSequenceDto
				CookingSequenceDto sequence = new CookingSequenceDto();
				int recipe_id = recipeService.findRecipeIdByMangeId(line_list[1]);
				sequence.setRecipe_id(recipe_id);
				sequence.setSequence_text(line_list[2]);
				sequence.setSequence_step_no(Integer.valueOf(line_list[3]));

				// Insert sequence into database
				recipeService.insertRecipeSequence(sequence);

				// Break if specific condition is met
				if (line_list[0].equals("30206") || line.equals(
						"30206,1751398,버터를 바른틀에 반죽을 80~90%정도 채워지게 붓습니다. 아몬드 슬라이스를 넣은후 180도씨 예열오븐에 25~30분간 굽습니다.오븐,4")) {
					System.out.println("끝");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error parsing number");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error");
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@GetMapping("/recipe_list")
	public String recipe_list_page(@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page_no, Model model,
			@RequestParam(value = "method", defaultValue = "") String method,
			@RequestParam(value = "status", defaultValue = "") String status,
			@RequestParam(value = "ingre", defaultValue = "") String ingre) {

		int recipe_list_size = recipeService.selectAll(keyword,method,status,ingre);
		PagingSearch pagingSearch = new PagingSearch(recipe_list_size, page_no);
		pagingSearch.setKeyword(keyword);
		pagingSearch.setMethod(method);
		pagingSearch.setIngre(ingre);
		pagingSearch.setStatus(status);
		List<RecipeDto> recipe_list = recipeService.selectRecipeByPagingSearch(pagingSearch);

		model.addAttribute("paging", pagingSearch);
		model.addAttribute("recipe_list", recipe_list);
		// 필터링
		List<String> ingre_list = recipeService.getIngre();
		model.addAttribute("ingre_list", ingre_list);

		List<String> method_list = recipeService.getMethod();
		model.addAttribute("method_list", method_list);

		List<String> status_list = recipeService.getStatus();
		model.addAttribute("status_list", status_list);

		return "recipe_list";
	}
	// 가장 최근 수정 2024-09-04
	// 2024 - 09- 13 필터링 기능 추가

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

	@GetMapping("/recipe/edit")
	public String recipe_edit(@RequestParam("recipe_id") int recipe_id, Model model, Principal principal) {

		// 요리방법
		List<String> method_list = recipeService.getMethod();
		model.addAttribute("method_list", method_list);

		// 요리 상황
		List<String> status_list = recipeService.getStatus();
		model.addAttribute("status_list", status_list);

		// 메인 재료
		List<String> ingre_list = recipeService.getIngre();
		model.addAttribute("ingre_list", ingre_list);

		// 레시피 정보
		RecipeDto recipe = recipeService.findRecipeByRecipe_id(recipe_id);
		model.addAttribute("recipe", recipe);

		// 레시피 재료 리스트
		List<Recipe_ingre> recipe_ingre_list = recipeService.selectRecipeIngreByRecipeId(recipe_id);
		model.addAttribute("recipe_ingre_list",recipe_ingre_list);

		// 요리 순서 및 내용
		List<CookingSequenceDto> cook_sequence_list = recipeService.selectRecipeSequenceAndTextByRecipeId(recipe_id);
		model.addAttribute("cook_sequence_list", cook_sequence_list);
		return "recipe_edit";
	}
	@ResponseBody
	@PostMapping("/recipe/edit")
	public String editRecipe(RecipeDto recipeDto, @RequestParam("recipe_ingre") String recipeIng,
							 @RequestParam("cookingSequenceDto") String cookingSequence,
							 @RequestParam Map<String, MultipartFile> sequence_img, Principal principal) throws IOException {

		System.out.println(recipeDto.toString());
		System.out.println(recipeIng.toString());
		System.out.println(cookingSequence.toString());
		System.out.println(sequence_img.toString());
		recipeService.updateRecipe(recipeDto);

		// 2. 저장한 레시피 id 가져오기
		int recipe_id = recipeDto.getRecipe_id();

		// 기존 재료 삭제
		recipeService.deleteIngreByRecipeId(recipe_id);
		// 기존 조리 순서 삭제
		recipeService.deleteSequenceImgByRecipeId(recipe_id);

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
				String path = "C:\\TeamProject\\omm\\omm_git\\omm\\src\\main\\resources\\static\\img\\"; //C:\\TeamProject\\omm_git\\omm\\src\\main\\resources\\static\\img\\
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
		return String.valueOf(recipe_id);
	}
//	public String editIngre(@RequestBody List<Recipe_ingre> ingre_list){
//		ingre_list.forEach(d-> System.out.println(d.toString()));
//		ingre_list.forEach((ingre)->{
//			Recipe_ingre db_ingre = recipeService.selectIngreByIngreId(ingre.getIngre_id());
//			db_ingre.setIngre_name(ingre.getIngre_name());
//			db_ingre.setIngre_info(ingre.getIngre_info());
//			recipeService.updateIngre(db_ingre);
//		});
//		return "";
//	}

	@ResponseBody
	@PostMapping("/recipe/insert")
	public int recipeInsert(RecipeDto recipeDto, @RequestParam("recipe_ingre") String recipeIng,
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
				String path = "C:\\TeamProject\\name\\omm_git\\omm\\src\\main\\resources\\static\\img\\"; // C:\\TeamProject\\omm_git\\omm\\src\\main\\resources\\static\\img\\
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
		return recipe_id;
	}

	// 레시피 detail
	@GetMapping("/recipe_list/{recipe_id}")
	public String recipe_detail(@PathVariable("recipe_id") int recipe_id,
			@RequestParam(value = "page", defaultValue = "1") int page_no,
			@RequestParam(value = "keyword", defaultValue = "") String keyword, Model model, Principal principal) {
		model.addAttribute("page", page_no);
		model.addAttribute("keyword", keyword);
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

	// 조회수를 증가시킬때 매핑되는 메서드
	// 1. table_name 조회수 변경할 테이블 이름 ex) tbl_recipe
	// 2. column_name 조회수 컬럼 명 ex) recipe_view
	// 3. target_column where조건 컬럼 명 ex) recipe_id
	// 4. target_id where 조건 값 ex) 168471
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
	// 1. reference_type ex) 1(recipe) 2(food) 3(inquire)
	// 2. target_id ex) 16874
	// 3. checked 추천 체크박스가 체크 되어 있는지 여부 ex) true, false
	@ResponseBody
	@PostMapping("/likeSet")
	public boolean likeSet(@RequestParam("reference_type") int reference_type,
			@RequestParam("target_id") String target_id, @RequestParam("checked") boolean checked,
			Principal principal) {
		String user_id = "";
		try {
			user_id = principal.getName();
		} catch (Exception e) {
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

	// 찜 목록 보기
	@GetMapping("/recipe/like")
	public String likePage(Model model, Principal principal,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page_no) {
		String user_id = principal.getName();
		int recipe_list_size = recipeService.selectLikeRecipeAll(keyword, user_id);
		PagingSearch pagingSearch = new PagingSearch(recipe_list_size, page_no);
		pagingSearch.setKeyword(keyword);

		List<RecipeDto> recipe_list = recipeService.selectLikeRecipe(user_id, pagingSearch);

		model.addAttribute("paging", pagingSearch);
		model.addAttribute("recipe_list", recipe_list);

		return "recipe_list";
	}

	// 임시로 비번 업데이트
//	@GetMapping("/encode_admin")
//	public String asdf() {
//		String encode = passwordEncoder.encode("123123123");
//		dao.asdf(encode);
//		return "redirect:/";
//	}
	@ResponseBody
	@PostMapping("/updateComment")
	public String updateComment(@RequestParam("commentId") int commentId,
			@RequestParam("newCommentText") String newCommentText) {
		try {
			boolean updated = recipeService.updateComment(newCommentText, commentId); // 댓글 업데이트
			if (updated) {
				return "{\"success\": true, \"message\": \"댓글이 수정되었습니다.\"}"; // 성공 응답
			} else {
				return "{\"success\": false, \"message\": \"댓글 수정에 실패했습니다.\"}"; // 실패 응답
			}
		} catch (Exception e) {
			return "{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}"; // 예외 처리 응답
		}
	}

	@ResponseBody
	@PostMapping("deleteComment")
	public String deleteComment(@RequestParam("commentId") int commentId) {
		try {
			boolean deleted = recipeService.deleteComment(commentId); // 댓글 삭제
			if (deleted) {
				return "{\"success\": true, \"message\": \"댓글이 삭제되었습니다.\"}"; // 성공 응답
			} else {
				return "{\"success\": false, \"message\": \"댓글 삭제에 실패했습니다.\"}"; // 실패 응답
			}
		} catch (Exception e) {
			return "{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}"; // 예외 처리 응답
		}
	}

	@ResponseBody
	@PostMapping("deleteRecipe")
	public String deleteRecipe(@RequestParam("recipe_id") int recipe_id) {
		try {
			boolean deleted = recipeService.deleteRecipe(recipe_id); // 레시피 삭제
			if (deleted) {
				return "{\"success\": true, \"message\": \"레시피가 삭제되었습니다.\"}"; // 성공 응답
			} else {
				return "{\"success\": false, \"message\": \"레시피 삭제에 실패했습니다.\"}"; // 실패 응답
			}
		} catch (Exception e) {
			return "{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}"; // 예외 처리 응답
		}
	}
	
	@GetMapping("/recipe_recommend")
	public String recipe_recommend(){
		return "recipe_recommend";
	}

}