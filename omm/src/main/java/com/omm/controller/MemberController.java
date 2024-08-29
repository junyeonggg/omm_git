package com.omm.controller;

import com.omm.dto.MemberDto;
import com.omm.service.MemberService;
import com.omm.service.UserSecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService member_service;
    private final UserSecurityService user_service;

    @GetMapping("/join")
    public String JoinPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "join";
    }

    @PostMapping("join")
    public String JoinPost(@Valid MemberDto dto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            // 유효성 검사가 실패한 경우, 에러 메시지를 모델에 추가
            model.addAttribute("errors", errors.getAllErrors());
            return "join"; // 유효성 검사 실패 시 다시 가입 페이지로 돌아감
        }
        user_service.create(dto);
        return "redirect:/index";
    }
    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam(value = "data") String user_id) {
        return String.valueOf(member_service.check_id(user_id));
    }
    @GetMapping("/checkNickname")
    @ResponseBody
    public String checkNickname(@RequestParam(value = "data") String user_nickname) {
        return String.valueOf(member_service.check_nickname(user_nickname));
    }
    @GetMapping("/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam(value = "data") String user_email) {
        return String.valueOf(member_service.check_email(user_email));
    }
    @PostMapping("/sendVerificationEmail")
    @ResponseBody
    public String sendVerificationEmail(@RequestParam(value = "user_email") String user_email) {
        try {
            user_service.registerUser(user_email);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    @PostMapping("/verifyEmailCode")
    @ResponseBody
    public String verifyEmailCode(@RequestParam(value = "user_email") String user_email, @RequestParam(value = "code") String code) {
        boolean isVerified = user_service.verifyEmailCode(user_email, code);
        if (isVerified) {
            return "{\"success\": true}";
        } else {
            return "{\"success\": false, \"message\": \"인증 코드가 유효하지 않습니다.\"}";
        }
    }

}
