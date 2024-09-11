package com.omm.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class InquireDto {
	private int inquire_id;
	private String user_id;
	private String inquire_title;
	private String inquire_content;
	private String inquire_create_date;

	public InquireDto() {
		LocalDate now = LocalDate.now();
		this.inquire_create_date = now.toString();

	}
}
