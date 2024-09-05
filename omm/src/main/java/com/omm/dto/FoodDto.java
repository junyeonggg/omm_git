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

    private int foodId;
    private String foodName;
    private String foodImg;
    private String foodLprice;
    private String foodMallName;
    private String foodProductId;
    private int categoryId;
}