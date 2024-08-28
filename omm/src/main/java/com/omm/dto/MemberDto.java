package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_nickname;
    private String user_email;
    private String user_addr;
    private String user_tel;
    private String user_gender;
    private int user_permit;
}
