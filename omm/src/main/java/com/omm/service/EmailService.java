package com.omm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender email_sender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("whdans1542@naver.com"); // 발송자 이메일 주소
        message.setTo(to); // 수신자 이메일 주소
        message.setSubject(subject);
        message.setText(text);
        email_sender.send(message);
    }
}
