package com.teamproject.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.entity.Users;

@Mapper
public interface UsersMapper {
	boolean existsByEmail(String email); // 회원조회(유무)
	void saveUsers(Users users); // 회원가입
	Users findByEmail(String email); // 회원조회
	void changeNickname(@Param("nickname") String name, @Param("userId") Long userId);
	String findPasswordHashByUserId(Long userId);
	void changePW(@Param("userId") Long userId, @Param("encodedPW") String encodedPW);
}