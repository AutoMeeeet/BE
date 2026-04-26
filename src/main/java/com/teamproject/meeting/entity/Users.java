package com.teamproject.meeting.entity;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
=======
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
>>>>>>> origin/dev
public class Users {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
<<<<<<< HEAD
    private String provider;
    private String profileImageUrl;
=======
    private Provider provider;
    private String profileImageUrl;
    private Role role;
>>>>>>> origin/dev
}