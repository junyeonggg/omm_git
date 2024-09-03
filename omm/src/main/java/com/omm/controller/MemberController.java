package com.omm.controller;

import com.omm.dto.MemberDto;
import com.omm.service.EmailService;
import com.omm.service.MemberService;
import com.omm.service.UserSecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService member_service;
    private final UserSecurityService user_service;
    private final PasswordEncoder passwordEncoder;
    private final EmailService email_service;
    private final HttpServletRequest request;
    private AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;

    private final String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
    @GetMapping("/join")
    public String JoinPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "join";
    }

    @ResponseBody
    @PostMapping("/join")
    public String JoinPost(@Valid MemberDto dto, Errors errors, Model model) {
        System.out.println(dto.toString());
        if (errors.hasErrors()) {
            return "registrationPage";
        }

        user_service.create(dto);
        return "true";
        //return "redirect:/";
    }
    @ResponseBody
    @PostMapping("/socialjoin")
    public String socialJoinPost(@Valid MemberDto dto, Errors errors, Model model, HttpSession session) {
        System.out.println("소셜회원가입 들어옴");
        System.out.println(dto.toString());
        // 회원가입 처리
        user_service.create(dto);


        // 홈 페이지로 리디렉션
        return "true";
    }


//    @PostMapping("/socialjoin")
//    public String socialJoinPost(@Valid MemberDto dto, Errors errors, Model model, HttpSession session) {
//        dto.setUser_pw("1234");
//        if (dto.getUser_id().contains("_")) {
//            user_service.create(dto);
//            session.setAttribute("user_id", dto.getUser_id());
//            session.setAttribute("user_name", dto.getUser_name());
//            session.setAttribute("user_email", dto.getUser_email());
//            return "redirect:/";
//        }
//        user_service.create(dto);
//        return "redirect:/";
//    }

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
    @GetMapping("/socialjoin")
    public String socialPage(){
        return "socialjoin";
    }

    @GetMapping("/login/oauth2/code/{social}")
    public String social_login(@RequestParam("code") String code,@PathVariable("social") String social, Model model){
        System.out.println("code : "+code);
        System.out.println("social : " +social);
        // 인가코드 -> 엑세스 토큰
        String tokenUrl = "";
        String user_id = "";
        String user_name = "";
        String user_email = "";
        if(social.equals("google")){
            tokenUrl = "https://oauth2.googleapis.com/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", "931563116169-26l7rc649fjpeoaf7jo1p8bjvqloco45");
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
            user_id = userInfo.optString("sub");
            user_id = user_id + "_google";
            user_name = userInfo.optString("name");
            String givenName = userInfo.optString("given_name");
            String familyName = userInfo.optString("family_name");
            user_email = userInfo.optString("email");
            Boolean emailVerified = userInfo.optBoolean("email_verified");
            String picture = userInfo.optString("picture");
            String locale = userInfo.optString("locale");

            // 데이터 출력
            System.out.println("User ID: " + user_id);
            System.out.println("Name: " + user_name);
            System.out.println("Email: " + user_email);
        }else if(social.equals("kakao")){
            tokenUrl = "https://kauth.kakao.com/oauth/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", "6379a78e09703b800a317f97af0045c0");
            body.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
            body.add("code", code);

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
            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
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

            System.out.println("User Info Response: " + userInfoResponse.getBody());

            user_id = userInfo.optString("id");  // 사용자 ID
            user_id = user_id + "_kakao";  // "_kakao"로 식별자 추가

            // 데이터 출력
            System.out.println("User ID: " + user_id);
        }else if(social.equals("naver")){
            tokenUrl = "https://nid.naver.com/oauth2.0/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", "2qwNxrPteLgOO7496Fet");
            body.add("client_secret", "Zjb829URkN");
            body.add("redirect_uri", "http://localhost:8080/login/oauth2/code/naver");
            body.add("code", code);

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
            String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
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
            JSONObject responseObject = userInfo.getJSONObject("response");
            System.out.println("User Info Response: " + userInfoResponse.getBody());
            user_id = responseObject.optString("id");  // 사용자 ID
            user_id = user_id + "_naver";  // "_naver"로 식별자 추가
            System.out.println("User ID: " + user_id);
        }

        model.addAttribute("user_id", user_id);
        model.addAttribute("user_name", user_name);
        model.addAttribute("user_email", user_email);

        boolean user = !(member_service.check_id(user_id));
        model.addAttribute("user",user);
        return "socialjoin";
    }
}



