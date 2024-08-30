package com.omm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FoodDto {

    private Integer foodId;

    @JsonProperty("title")
    private String foodTitle; // JSON의 "title" 필드에 매핑

    @JsonProperty("link")
    private String foodLink; // JSON의 "link" 필드에 매핑

    @JsonProperty("image")
    private String foodImage; // JSON의 "image" 필드에 매핑

    @JsonProperty("lprice")
    private String foodLprice; // JSON의 "lprice" 필드에 매핑

    @JsonProperty("hprice")
    private String foodHprice; // JSON의 "hprice" 필드에 매핑

    @JsonProperty("mallName")
    private String foodMallName; // JSON의 "mallName" 필드에 매핑

    @JsonProperty("productId")
    private String foodProductId; // JSON의 "productId" 필드에 매핑

    @JsonProperty("productType")
    private String foodProductType; // JSON의 "productType" 필드에 매핑

    @JsonProperty("brand")
    private String foodBrand; // JSON의 "brand" 필드에 매핑

    @JsonProperty("maker")
    private String foodMaker; // JSON의 "maker" 필드에 매핑

    @JsonProperty("category1")
    private String foodCategory1; // JSON의 "category1" 필드에 매핑

    @JsonProperty("category2")
    private String foodCategory2; // JSON의 "category2" 필드에 매핑

    @JsonProperty("category3")
    private String foodCategory3; // JSON의 "category3" 필드에 매핑

    @JsonProperty("category4")
    private String foodCategory4; // JSON의 "category4" 필드에 매핑


    public Integer categoryId;
}