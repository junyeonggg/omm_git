package com.omm.controller;

import com.omm.dto.MemberDto;
import com.omm.service.EmailService;
import com.omm.service.MemberService;
import com.omm.service.UserSecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService member_service;
    private final UserSecurityService user_service;
    private final EmailService email_service;
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
    @ResponseBody
    @PostMapping("/sendmail")
    public boolean sendmail(@RequestParam("user_email")String user_email){
        boolean flag = true;
        String subject = "오늘 뭐먹지 회원가입 이메일 인증 번호 입니다.";
        String code = UUID.randomUUID().toString().substring(0,8);
        String text = code;
        try{
            email_service.sendEmail(user_email,subject,text);
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        email_service.delete_code(user_email);
        email_service.email_code_save(user_email,code);

        return flag;
    }
    // 이메일인증번호 확인
    @PostMapping("/checkEmailCode")
    @ResponseBody
    public boolean checkEmail(@RequestParam(value = "user_email") String user_email,@RequestParam("code")String code) {
        String email_by_code = email_service.email_code_check(code);
        if(user_email.equals(email_by_code)){
            return true;
        } else {
            return false;
        }
    }
}
