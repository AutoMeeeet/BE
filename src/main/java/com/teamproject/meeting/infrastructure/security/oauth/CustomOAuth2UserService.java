package com.teamproject.meeting.infrastructure.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UsersRepositoryPort usersRepositoryPort;
	
	public CustomOAuth2UserService(UsersRepositoryPort usersRepositoryPort) {
		
		this.usersRepositoryPort = usersRepositoryPort;
	}
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String email = oAuth2Response.getEmail();
        Users existData = usersRepositoryPort.findByEmail(email);
        
        if(existData == null) {
        	// 서버 DB 저장
        	Users user = new Users();
        	user.setEmail(email);
        	user.setNickname(oAuth2Response.getName());
        	user.setRole(Role.USERS);
        	user.setProvider(Provider.valueOf(oAuth2Response.getProvider().toUpperCase()));
        	
        	usersRepositoryPort.saveUsers(user);
        }
        
        	// DTO로 Provider에게 던져주기
        	OAuthJoinDto userDto = new OAuthJoinDto();
        	userDto.setEmail(oAuth2Response.getEmail());
        	userDto.setNickname(oAuth2Response.getName());
        	userDto.setRole((Role.USERS).toString());

            return new CustomOAuth2User(userDto);
        
    }
}