package com.omm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mysql.cj.Session;
import com.omm.dto.InquireDto;
import com.omm.service.InquireService;

@Controller
public class InquireController {
	@Autowired
	private InquireService inquireService;
	//문의 게시판 이동
	@GetMapping("/inquire")
	public String inquire_page(Model model) {
		List<InquireDto>inquire_list = inquireService.selectAll();
		model.addAttribute("inquire_list", inquire_list);
		return "inquire_list";
	}
	
	//문의 상세보기
	@GetMapping("/inquire/{id}")
	public String inquire_detail_page(@PathVariable("id") int id,Model model) {
		InquireDto inquire = inquireService.selectInquireById(id);
		model.addAttribute("inquire", inquire);
		return "inquire_detail";
		
	}
	
	//문의 작성페이지
	@GetMapping("/inquire/write")
	public String inquire_write_page(Model model,Principal principal) {
		model.addAttribute("user_id", principal.getName());
		return "inquire_detail";
		
	}
	
}
