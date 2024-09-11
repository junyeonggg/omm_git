package com.omm.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Integer categoryId;
    private String categoryName;
    private Integer parentCategoryId;
    private List<CategoryDto> children = new ArrayList<>();;
}
