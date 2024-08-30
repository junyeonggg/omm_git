package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Integer category_id;
    private String category_name;
    private Integer parent_category_id;
}
