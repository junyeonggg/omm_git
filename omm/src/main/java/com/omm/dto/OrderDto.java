package com.omm.dto;

import lombok.*;

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
