package com.omm.controller;

import com.omm.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/")
    public String indexPage(){
        return "index";
    }
//    @GetMapping("/adminUpdate")
//    public String asdf(){
//        memberDao.updatePassword("admin",passwordEncoder.encode("1234"));
//        System.out.println("업데이트 완료");
//        return "index";
//    }
}
