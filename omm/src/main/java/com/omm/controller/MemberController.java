package com.omm.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omm.dto.MemberDto;
import com.omm.service.MemberService;
import com.omm.service.UserSecurityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
    public String sendVerificationEmail(@RequestBody Map<String, String> requestBody) {
        // 'email' 키를 사용하여 값 추출
        String userEmail = requestBody.get("email");

        try {
            // 이메일 주소로 사용자 등록 로직 호출
            user_service.registerUser(userEmail);
            return "{\"success\": true}";
        } catch (Exception e) {
            // 예외가 발생한 경우 오류 메시지 반환
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
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token, Model model) {
        boolean isTokenValid = user_service.validateToken(email, token); // 토큰 유효성 검증
        if (isTokenValid) {
            model.addAttribute("email", email); // 이메일을 모델에 추가
            return "verifyEmailPage"; // 인증 번호 입력 페이지로 리디렉션
        } else {
            model.addAttribute("message", "인증 링크가 유효하지 않거나 만료되었습니다.");
            return "verificationFailure"; // 인증 실패 페이지
        }
    }

}
