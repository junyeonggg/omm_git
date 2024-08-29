package com.omm.dto;

import lombok.*;

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
