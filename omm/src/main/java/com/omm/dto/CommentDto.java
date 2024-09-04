package com.omm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private int comment_id;
    private String user_id;
    private String comment_content;
    private String comment_create_date;
    private int target_id;
    private int parent_comment_id;
    private double comment_rating;
    private int reference_type;

    public CommentDto(){
        LocalDate now = LocalDate.now();
        this.comment_create_date = now.toString();

    }

}
