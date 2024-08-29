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
public class OrderDto {
    private int order_id;
    private String user_id;
    private String order_date;
}
