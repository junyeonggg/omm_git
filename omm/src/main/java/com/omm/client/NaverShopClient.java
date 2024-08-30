package com.omm.client;

import com.omm.dto.FoodDto;
import com.omm.dto.NaverShopResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(name = "naverShopClient", url = "https://openapi.naver.com/v1/search")
public interface NaverShopClient {
    @GetMapping("/shop.json")
    NaverShopResponse searchShop(
            @RequestParam("query") String query,
            @RequestParam("display") int display,
            @RequestHeader("X-Naver-Client-Id") String clientId,
            @RequestHeader("X-Naver-Client-Secret") String clientSecret
    );
}
