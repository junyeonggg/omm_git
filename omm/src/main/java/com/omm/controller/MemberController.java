package com.omm.controller;

import com.omm.dto.MemberDto;
import com.omm.service.EmailService;
import com.omm.service.MemberService;
import com.omm.service.UserSecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService member_service;
    private final UserSecurityService user_service;
    private final EmailService email_service;
    private final String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
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
        return "redirect:/";
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
    public boolean sendmail(@RequestParam("user_email") String user_email) {
        boolean flag = true;
        String subject = "오늘 뭐먹지 회원가입 이메일 인증 번호 입니다.";
        String code = UUID.randomUUID().toString().substring(0, 8);
        String text = code;
        try {
            email_service.sendEmail(user_email, subject, text);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        email_service.delete_code(user_email);
        email_service.email_code_save(user_email, code);

        return flag;
    }

    // 이메일인증번호 확인
    @PostMapping("/checkEmailCode")
    @ResponseBody
    public boolean checkEmail(@RequestParam(value = "user_email") String user_email, @RequestParam("code") String code) {
        String email_by_code = email_service.email_code_check(code);
        if (user_email.equals(email_by_code)) {
            return true;
        } else {
            return false;
        }
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/login/oauth2/code/{social}")
    public String social_login(@RequestParam("code") String code,@PathVariable("social") String social){
        System.out.println("code : "+code);
        System.out.println("social : " +social);
        // 인가코드 -> 엑세스 토큰
        String tokenUrl = "";
        if(social.equals("google")){
            tokenUrl = "https://oauth2.googleapis.com/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", "931563116169-26l7rc649fjpeoaf7jo1p8bjvqloco45.apps.googleusercontent.com");
            body.add("client_secret", "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");
            body.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
            body.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            // 액세스 토큰 요청
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 액세스 토큰 추출
            JSONObject jsonResponse = new JSONObject(response.getBody());
            String accessToken = jsonResponse.getString("access_token");

            // 사용자 정보 요청
            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    userInfoEntity,
                    String.class
            );

            // 사용자 정보 추출
            JSONObject userInfo = new JSONObject(userInfoResponse.getBody());
            String userId = userInfo.optString("sub");
            String name = userInfo.optString("name");
            String givenName = userInfo.optString("given_name");
            String familyName = userInfo.optString("family_name");
            String email = userInfo.optString("email");
            Boolean emailVerified = userInfo.optBoolean("email_verified");
            String picture = userInfo.optString("picture");
            String locale = userInfo.optString("locale");

            // 데이터 출력
            System.out.println("User ID: " + userId);
            System.out.println("Name: " + name);
            System.out.println("Given Name: " + givenName);
            System.out.println("Family Name: " + familyName);
            System.out.println("Email: " + email);
            System.out.println("Email Verified: " + emailVerified);
            System.out.println("Picture: " + picture);
            System.out.println("Locale: " + locale);
        }else if(social.equals("kakao")){
            tokenUrl = "";
        }else if(social.equals("naver")){
            tokenUrl = "";
        }

        return "index";
    }
}

