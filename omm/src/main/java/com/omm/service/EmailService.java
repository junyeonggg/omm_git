package com.omm.service;

import com.omm.dao.MemberDao;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender email_sender;
    private final MemberDao member_dao;
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("whdans1542@naver.com"); // 발송자 이메일 주소
        message.setTo(to); // 수신자 이메일 주소
        message.setSubject(subject);
        message.setText(text);
        email_sender.send(message);
    }
    public void email_code_save(String user_email, String code){
        member_dao.emailCodeSave(user_email,code);
    }

    public String email_code_check(String code) {
        return member_dao.emailCodeCheck(code);
    }

    public void delete_code(String user_email) {
        int cnt = member_dao.delete_check(user_email);
        if(cnt > 0){
            member_dao.delete_code(user_email);
        }
    }
}
