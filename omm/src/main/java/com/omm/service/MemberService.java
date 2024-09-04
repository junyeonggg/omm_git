package com.omm.service;

import com.omm.dao.MemberDao;
import com.omm.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public MemberDto getMemberByName(MemberDto dto) {
       return member_dao.getByUserNameAndEmail(dto.getUser_name(), dto.getUser_email());
    }
}



