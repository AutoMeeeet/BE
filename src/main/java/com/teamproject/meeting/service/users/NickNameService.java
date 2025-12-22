package com.teamproject.meeting.service.users;

import org.springframework.stereotype.Service;

import com.teamproject.meeting.port.UsersRepositoryPort;

@Service
public class NickNameService {
	
	private final UsersRepositoryPort usersRepositoryPort;
	
	public NickNameService(UsersRepositoryPort usersRepositoryPort) {
		this.usersRepositoryPort = usersRepositoryPort;
	}
	
	public void changeNickname(String name, Long userId) {
		usersRepositoryPort.changeNickname(name, userId);
	}
}
