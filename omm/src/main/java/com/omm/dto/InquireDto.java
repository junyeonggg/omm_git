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
public class InquireDto {
    private int inquire_id;
    private String user_id;
    private String inquire_title;
    private String inquire_content;
    private String inquire_create_date;
}
