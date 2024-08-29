package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CookingSequenceDto {
    private int sequence_id;
    private int recipe_id;
    private String sequence_text;
    private int img_id;
    private int sequence_step_no;
}
