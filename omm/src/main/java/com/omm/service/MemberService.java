package com.omm.service;

import com.omm.dao.MemberDao;
import com.omm.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;


@RequiredArgsConstructor
@Service

public class MemberService {
    private final MemberDao member_dao;

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


}



