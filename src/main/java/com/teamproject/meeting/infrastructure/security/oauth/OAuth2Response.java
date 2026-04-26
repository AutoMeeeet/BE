package com.teamproject.meeting.infrastructure.security.oauth;

public interface OAuth2Response {
	
	// 제공자 (Ex. naver, google, ...)
    String getProvider();
    
    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    
    // email
    String getEmail();
    
    // name
    String getName();
}
