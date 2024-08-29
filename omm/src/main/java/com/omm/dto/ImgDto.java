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
public class ImgDto {
    private int img_id;
    private String img_name;
    private String img_org_name;
    private String img_path;
    private int target_id;
    private int reference_type;
}
