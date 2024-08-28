package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
    private int category_id;
    private String category_name;
    private int parent_category_id;
}
