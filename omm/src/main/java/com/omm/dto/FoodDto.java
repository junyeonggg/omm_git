package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FoodDto {
    private int food_id;
    private String food_name;
    private String food_link;
    private String food_img;
    private int food_lprice;
    private int food_hprice;
    private String food_mall_name;
    private String food_product_id;
    private String food_brand_name;
    private int category_id;
}
