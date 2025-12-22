package com.teamproject.meeting.infrastructure.security.local;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.port.UsersRepositoryPort;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepositoryPort userRepositoryPort;

    public CustomUserDetailsService(UsersRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
				
        Users userData = userRepositoryPort.findByEmail(email);

        if (userData == null) {
            throw new UsernameNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email);
        }

        return new CustomUserDetails(userData);
    }
}