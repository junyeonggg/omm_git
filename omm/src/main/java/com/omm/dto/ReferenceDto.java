package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReferenceDto {
    private int reference_type;
    private String reference_name;
}
