package com.teamproject.meeting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String provider;
    private String profileImageUrl;
}