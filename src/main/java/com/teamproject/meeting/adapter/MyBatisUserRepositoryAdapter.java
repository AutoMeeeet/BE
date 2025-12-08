package com.teamproject.meeting.adapter;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.mapper.UsersMapper;
import com.teamproject.meeting.port.UsersRepositoryPort;

@Repository
public class MyBatisUserRepositoryAdapter implements UsersRepositoryPort {
	private final UsersMapper usersMapper;
	
	public MyBatisUserRepositoryAdapter(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return usersMapper.existsByEmail(email);
	}
	
	@Override
    public void saveUsers(Users user) {
        usersMapper.saveUsers(user);
    }
	
	@Override
	public Users findByEmail(String email) {
		return usersMapper.findByEmail(email);
	}
}