package com.omm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Controller
public class ShopController {

    private final RestTemplate restTemplate;
    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    public ShopController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/shop")
    public String getShopPage(Model model) {
        String category = "과자";
        String apiUrl = "https://openapi.naver.com/v1/search/shop.json";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("query", category)
                .queryParam("display", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        // JSON 데이터를 Thymeleaf에서 처리하기 위해 모델에 추가
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            model.addAttribute("products", jsonNode.get("items"));
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("products", null);
        }

        return "shop"; // Thymeleaf 템플릿 이름
    }
}
