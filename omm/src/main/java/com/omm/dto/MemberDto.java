package com.omm.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class MemberDto {

    @Size(min = 4, max = 12, message = "아이디는 4~12자리여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "아이디는 소문자와 숫자만 포함해야 합니다.")
    private String user_id;

    @Size(min = 8, max = 16, message = "비밀번호는 8~16자리여야 합니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&+=]).*[a-zA-Z0-9!@#$%^&+=]{8,16}$", message = "비밀번호는 하나의 특수문자를 포함해야 하며, 대문자와 한글은 포함될 수 없습니다.")
    private String user_pw;

    private String user_name;
    private String user_nickname;
    private String user_email;
    private String user_addr_zip;
    private String user_addr;
    private String user_addr_detail;
    private String user_tel;
    private String user_gender;
    private String user_birth;
    private int user_permit;
}
