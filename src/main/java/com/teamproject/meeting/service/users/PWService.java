package com.teamproject.meeting.service.users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.port.UsersRepositoryPort;

@Service
public class PWService {
	
	private final UsersRepositoryPort usersRepositoryPort;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public PWService(UsersRepositoryPort usersRepositoryPort, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepositoryPort = usersRepositoryPort;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Transactional
	public void changePW(Long userId, String currentPW, String newPW) {
		
		String storedHash = usersRepositoryPort.findPasswordHashByUserId(userId);
		
		if(!bCryptPasswordEncoder.matches(currentPW, storedHash)) {
			throw new IllegalArgumentException("현재 비밀번호 불일치");
		}
		
		String encodedPW = bCryptPasswordEncoder.encode(newPW);
		
		usersRepositoryPort.changePW(userId, encodedPW);
	}
}