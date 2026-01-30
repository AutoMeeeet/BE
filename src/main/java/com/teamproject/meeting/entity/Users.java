package com.teamproject.meeting.entity;

import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private Provider provider;
    private String profileImageUrl;
    private Role role;
}