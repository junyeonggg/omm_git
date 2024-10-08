package com.omm.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omm.dto.InquireDto;
import com.omm.dto.PagingSearch;
import com.omm.service.InquireService;
import com.omm.service.MemberService;
import com.omm.service.RecipeService;

@Controller
public class InquireController {
	@Autowired
	private InquireService inquireService;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private MemberService memberService;

	// 문의 게시판 이동
	@GetMapping("/inquire")
	public String inquire_page(Model model, Principal principal,@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page_no) {
		// 페이징
		int totCnt = inquireService.getTotCnt();
		PagingSearch paging = new PagingSearch(totCnt, page_no);
		paging.setKeyword(keyword);
		model.addAttribute("paging", paging);
		
		
		
		String user_nickname = null;
		if (principal != null) {
			String user_id = principal.getName();
			user_nickname = recipeService.getUserNicknameByUserId(user_id);
		}
		model.addAttribute("user_nickname", user_nickname);
		List<InquireDto> inquire_list = inquireService.selectAll(paging);
		model.addAttribute("inquire_list", inquire_list);
		
		
		
		
		return "inquire_list";
	}

	// 문의 상세보기
	@GetMapping("/inquire/{inquire_id}")
	public String inquire_detail_page(@PathVariable("inquire_id") int inquire_id, Model model) {
		HashMap<String, Object> inquireDto = inquireService.selectInquireById(inquire_id);
		model.addAttribute("inquire", inquireDto);
		return "inquire_detail";

	}

	// 문의 작성페이지
	@GetMapping("/inquire/write")
	public String inquire_write_page(Model model, Principal principal) {
		String user_nickname = null;
		if (principal != null) {
			String user_id = principal.getName();
			user_nickname = recipeService.getUserNicknameByUserId(user_id);
		}
		model.addAttribute("user_nickname", user_nickname);
		return "inquire_write";

	}
	
	// 문의 작성 post
	@PostMapping("/inquire/write")
	public String inquire_write(InquireDto inquireDto,Principal principal) {
		String user_id = principal.getName();
		inquireDto.setUser_id(user_id);
		inquireService.insertInquire(inquireDto);
		return "redirect:/inquire";
	}
	@PostMapping("/inquire/update")
	public String inquire_update(InquireDto inquireDto, Principal principal) {
		inquireDto.setUser_id(principal.getName());
		inquireService.updateForm(inquireDto);
		return "redirect:/inquire/"+inquireDto.getInquire_id();
	}
	
	
	// 문의 삭제
	@ResponseBody
	@PostMapping("/inquire/delete")
	public String inquire_delete(@RequestParam("inquire_id") int inquire_id) {
		inquireService.deleteInquire(inquire_id);
		return "asd";
		
	}

}
