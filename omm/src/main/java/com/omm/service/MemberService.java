package com.omm.service;

import com.omm.dao.MemberDao;
import com.omm.dto.InquireDto;
import com.omm.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;


@RequiredArgsConstructor
@Service

public class MemberService {
    private final MemberDao member_dao;
    private final PasswordEncoder passwordEncoder;
    public boolean check_id(String user_id) {
        boolean result = false;
        if (member_dao.checkId(user_id) == 0) result = true;
        return result;
    }
    public boolean check_nickname(String user_nickname) {
        boolean result = false;
        if (member_dao.checkNickname(user_nickname) == 0) result = true;
        return result;
    }
    public boolean check_email(String user_email){
        boolean result = false;
        if (member_dao.checkEmail(user_email) == 0) result = true;
        return result;
    }
    public boolean checkTel(String user_tel){
        boolean result = false;
        if (member_dao.checkTel(user_tel) == 0) result = true;
        return result;
    }

    public MemberDto getMemberByName(MemberDto dto) {
       return member_dao.getByUserNameAndEmail(dto.getUser_name(), dto.getUser_email());
    }
    public boolean isValidUser(String user_id, String user_email) {
        int count = member_dao.checkIdAndEmailCount(user_id, user_email);
        return count > 0;
    }
    public void saveTemporaryPassword(String user_email, String encryptedPassword) {
        // 임시 비밀번호와 만료 시간을 저장
        member_dao.saveTemporaryPassword(user_email, encryptedPassword, new Timestamp(System.currentTimeMillis() + 3600000)); // 예: 1시간 후 만료
    }
    public void updatePassword(String user_id, String encryptedPassword) {
        member_dao.updatePassword(user_id, encryptedPassword);
    }
    public MemberDto getMemberInfo(String user_id) {
        MemberDto dto = new MemberDto();
        dto = member_dao.get_by_user_id(user_id);
        return dto;
    }
    public MemberDto editUserInPo(MemberDto dto) {
        MemberDto org = member_dao.get_by_user_id(dto.getUser_id());

        // 비밀번호 업데이트
        if (!dto.getUser_pw().isEmpty() && !dto.getUser_pw().equals(org.getUser_pw())) {
            String chngPw = passwordEncoder.encode(dto.getUser_pw());
            member_dao.updateUserpw(dto.getUser_id(), chngPw);
        }

        // 이메일 업데이트
        if (!dto.getUser_email().equals(org.getUser_email())) {
            member_dao.updateUserEmail(dto.getUser_id(), dto.getUser_email());
        }

        // 주소 업데이트
        if (!dto.getUser_addr().equals(org.getUser_addr())) {
            member_dao.updateUserAddress(dto.getUser_id(), dto.getUser_addr(), dto.getUser_addr_zip(), dto.getUser_addr_detail());
        }

        // 전화번호 업데이트
        if (!dto.getUser_tel().equals(org.getUser_tel())) {
            member_dao.updateUserTel(dto.getUser_id(), dto.getUser_tel());
        }

        // 닉네임 업데이트
        if (!dto.getUser_nickname().equals(org.getUser_nickname())) {
            member_dao.updateUserNickname(dto.getUser_id(), dto.getUser_nickname());
        }
        return org;
    }
    public void unregistUser(String user_id){
        member_dao.delteUser(user_id);
    }
	public MemberDto getMemberByUserId(String name) {
		return member_dao.getMemberByUserId(name);
	}

}



