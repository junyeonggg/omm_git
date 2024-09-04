package com.omm.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.RedirectView;




@RequiredArgsConstructor
@Controller
public class MemberController {
    // 구글
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String googleAuthorizationGrantType;
    @Value("${spring.security.oauth2.client.registration.google.authorization-uri}")
    private String googleAuthorizationUri;


    // 네이버
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String naverAuthorizationGrantType;
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String naverAuthorizationUri;

    // 카카오
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    // @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    // private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoAuthorizationGrantType;
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    private final MemberService member_service;
    private final UserSecurityService user_service;
    private final EmailService email_service;

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
        String text = "인증번호 : " + code;
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
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/login/{social}")
    public RedirectView loginPage(Model model, @PathVariable("social") String social) {

        if (social.equals("google")) {
            return new RedirectView(googleAuthorizationUri + "?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUri + "&response_type=code&scope=email profile");
        } else if (social.equals("kakao")) {
            return new RedirectView(kakaoAuthorizationUri + "?client_id=" + kakaoClientId + "&redirect_uri=" + kakaoRedirectUri + "&response_type=code");

        } else if (social.equals("naver")) {
            return new RedirectView(naverAuthorizationUri + "?client_id=" + naverClientId + "&redirect_uri=" + naverRedirectUri + "&response_type=code");
        } else {
            return new RedirectView("http://localhost:8080/login");
        }
    }

    @GetMapping("/socialjoin")
    public String socialPage() {
        return "socialjoin";
    }

    @GetMapping("/login/oauth2/code/{social}")
    public String social_login(@RequestParam("code") String code, @PathVariable("social") String social, Model model) {
        String tokenUrl;
        String user_id = "";
        String user_name = "";
        String user_email = "";
        // 인가코드 -> 엑세스 토큰
        if (social.equals("google")) {
            tokenUrl = "";
            tokenUrl = "https://oauth2.googleapis.com/token";
            RestTemplate restTemplate = new RestTemplate();
            //hi
            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", googleClientId);
            body.add("client_secret", googleClientSecret);
            body.add("redirect_uri", googleRedirectUri);
            body.add("grant_type", googleAuthorizationGrantType);

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
        } else if (social.equals("kakao")) {
            tokenUrl = "https://kauth.kakao.com/oauth/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", kakaoAuthorizationGrantType);
            body.add("client_id", kakaoClientId);
            body.add("redirect_uri", kakaoRedirectUri);
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
        } else if (social.equals("naver")) {
            tokenUrl = "https://nid.naver.com/oauth2.0/token";
            RestTemplate restTemplate = new RestTemplate();

            // 요청 데이터 설정
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", naverAuthorizationGrantType);
            body.add("client_id", naverClientId);
            body.add("client_secret", naverClientSecret);
            body.add("redirect_uri", naverRedirectUri);
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
        model.addAttribute("user", user);
        return "socialjoin";
    }

    @GetMapping("/find_id")
    public String findId() {
        return "find_id";
    }

    @GetMapping("/find_pw")
    public String findPw() {
        return "find_pw";
    }

    @PostMapping("/find_id")
    public String findIdPost(MemberDto dto, Model model) {
        System.out.println(dto.getUser_name());
        System.out.println(dto.getUser_email());
        String msg = "";
        MemberDto user_id = member_service.getMemberByName(dto);

        if (user_id == null) {
            msg = "해당하는 아이디가 존재하지 않습니다.!!!";
        } else {
            msg = "당신의 아이디는 " + user_id.getUser_id() + "입니다.";
        }
        model.addAttribute("msg", msg);
        return "find_id";
    }

    @PostMapping("/find_pw")
    @ResponseBody
    public boolean findPwPost(@RequestParam(value = "user_email") String user_email) {
        boolean flag = true;
        String subject = "오늘 뭐먹지 임시비밀번호입니다.";
        String code = UUID.randomUUID().toString().substring(0, 8);
        String text = "인증번호 : " + code;
        try {
            email_service.sendEmail(user_email, subject, text);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}


