package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.entity.Users;

public interface UsersRepositoryPort {
	boolean existsByEmail(String email); // 회원조회(유무)
	void saveUsers(Users users); // 회원가입
	Users findByEmail(String email); // 회원조회
	void changeNickname(String name, Long userId);
	String findPasswordHashByUserId(Long userId);
	void changePW(Long userId, String encodedPW);
}