package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private int comment_id;
    private String user_id;
    private String comment_content;
    private String comment_create_date;
    private int target_id;
    private int parent_comment_id;
    private double comment_rating;
    private int reference_type;
}
