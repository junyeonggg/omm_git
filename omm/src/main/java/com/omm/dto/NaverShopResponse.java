package com.omm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverShopResponse {
    @JsonProperty("items")
    private List<FoodDto> items; // 'items' 배열을 담기 위한 필드
}
