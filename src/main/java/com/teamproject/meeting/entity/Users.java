package com.teamproject.meeting.entity;

import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private Provider provider;
    private String profileImageUrl;
    private Role role;
}