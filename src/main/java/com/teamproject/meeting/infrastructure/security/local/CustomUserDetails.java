package com.teamproject.meeting.infrastructure.security.local;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;

public class CustomUserDetails implements UserDetails {

    private final Users users;

    public CustomUserDetails(Users users) {

        this.users = users;
    }
    
    public Long getUserId() { // 회원 인증 매개변수로 사용
    	return users.getUserId();
    }

    public String getNickname() {
    	return users.getNickname();
    }
    
    public Provider getProvider() {
    	return users.getProvider();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return users.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return users.getPassword();
    }

    @Override
    public String getUsername() {

        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}