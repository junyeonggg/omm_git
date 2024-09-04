package com.omm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recipe_ingre {
	private int ingre_id;
	private String ingre_type;
	private String ingre_name;
	private String ingre_info;
	private String mange_id;
}
