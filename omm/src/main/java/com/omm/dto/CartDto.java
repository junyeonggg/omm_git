package com.omm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    private int cart_id;
    private String user_id;
    private int food_id;
    private int food_quantity;
}
