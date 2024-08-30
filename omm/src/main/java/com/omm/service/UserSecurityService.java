package com.omm.service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.omm.dao.MemberDao;
import com.omm.dto.MemberDto;
import com.omm.role.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	private final MemberDao member_dao;
	private final PasswordEncoder passwordEncoder;
	private final EmailService email_service;
	
	public MemberDto create(MemberDto dto) {
		dto.setUser_pw(passwordEncoder.encode(dto.getUser_pw()));
		this.member_dao.insert_member(dto);
		return dto;
	}
	
	@Override			// 세큐리티내에서 name은 id를 의미한다 또한 view에서 name을  userid/password로 고정해야한다(세큐리티에서 id=userid/pw=password)
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException{
		MemberDto member = this.member_dao.get_by_user_id(user_id);
		if(member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(member.getUser_permit() == 9) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else if (member.getUser_permit() == 8) {
			authorities.add(new SimpleGrantedAuthority(UserRole.MANAGER.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		return new User(member.getUser_id(), member.getUser_pw(), authorities);
	}


	public void registerUser(String user_email) {
		// 인증 이메일 전송
		String subject = "이메일 인증";
		String verificationLink = generateVerificationLink(user_email);
		String text = "회원가입을 완료하려면 다음 링크를 클릭해 주세요: " + verificationLink;
		email_service.sendEmail(user_email, subject, text);
	}
	private String generateVerificationLink(String user_email) {
		// 인증 링크 생성 로직

		// UUID 생성
		String uuid = UUID.randomUUID().toString();

		return "http://example.com/verify?email=" + user_email + "&token=" + uuid;
	}
	public boolean verifyEmailCode(String user_email, String code) {
		// 인증 코드 검증 로직 추가
		// 예: 코드와 이메일을 비교하여 유효성을 확인
		// 코드 저장 및 관리 로직은 별도의 서비스나 DB에 저장할 수 있습니다.
		return true; // 실제 검증 로직에 맞게 반환값을 수정
	}

}
