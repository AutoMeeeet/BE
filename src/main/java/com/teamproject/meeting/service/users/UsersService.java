package com.teamproject.meeting.service.users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.dto.users.JoinReqDto;
import com.teamproject.meeting.dto.users.NickNameReqDto;
import com.teamproject.meeting.dto.users.PwReqDto;
import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;

@Service
public class UsersService {

	private final UsersRepositoryPort usersRepositoryPort;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UsersService(UsersRepositoryPort usersRepositoryPort, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepositoryPort = usersRepositoryPort;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Transactional
	public void joinProcess(JoinReqDto joinDto) {

	    if (usersRepositoryPort.existsByEmail(joinDto.getEmail())) {
	        throw new IllegalArgumentException("이미 가입된 이메일입니다.");
	    }

	    Users user = Users.builder()
	            .email(joinDto.getEmail())
	            .password(bCryptPasswordEncoder.encode(joinDto.getPassword())) // 암호화해서 주입
	            .nickname(joinDto.getNickname())
	            .provider(Provider.LOCAL)
	            .role(Role.USERS)
	            .build();

	    usersRepositoryPort.saveUsers(user);
	}
	
	@Transactional
	public void changePW(Long userId, PwReqDto pw) {
		
		String storedHash = usersRepositoryPort.findPasswordHashByUserId(userId);
		
		if(!bCryptPasswordEncoder.matches(pw.getCurrentPW(), storedHash)) {
			throw new IllegalArgumentException("현재 비밀번호 불일치");
		}
		
		String encodedPW = bCryptPasswordEncoder.encode(pw.getNewPW());
		
		usersRepositoryPort.changePW(userId, encodedPW);
	}
	
	@Transactional
	public void changeNickname(Long userId, NickNameReqDto name) {
		usersRepositoryPort.changeNickname(name.getNickname(), userId);
	}
}