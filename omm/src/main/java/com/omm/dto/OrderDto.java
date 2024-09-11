package com.omm.dto;

import java.time.LocalDateTime;

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
	private String order_id;
	private String user_id;
	private String order_date;
	private int food_id;

	public void orderDate(OrderDto orderDto) {
		LocalDateTime now = LocalDateTime.now();
		
		orderDto.setOrder_date(now.toString());
	}
}
