package com.teamproject.meeting.infrastructure.security.oauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private final OAuthJoinDto authJoinDto;

    public CustomOAuth2User(OAuthJoinDto authJoinDto) {

        this.authJoinDto = authJoinDto;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    // role 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return authJoinDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return authJoinDto.getEmail();
    }

    public String getUsername() {

        return authJoinDto.getNickname();
    }
}
