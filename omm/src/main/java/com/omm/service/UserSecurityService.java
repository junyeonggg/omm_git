package com.omm.service;


import com.omm.dao.MemberDao;
import com.omm.dto.MemberDto;
import com.omm.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

}
