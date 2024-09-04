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
public class OrderItemDto {
    private int order_item_id;
    private int order_id;
    private int food_id;
    private int food_quantity;
    private int food_price;
}
	