package com.teamproject.meeting.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {

        String securityJwtName = "JWT_Auth";
        
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);

        Components components = new Components().addSecuritySchemes(securityJwtName, new SecurityScheme()
                .name(securityJwtName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));     
    	
        return new OpenAPI()
                .info(new Info()
                        .title("Meeting API 목록")
                        .description("Meeting API 목록입니다.\n" +
                        		"1. **일반 API**: 헤더에 `Authorization: Bearer {access}` 포함\n" +
                                "2. **로그인**: 성공 시 헤더(Access), 쿠키(Refresh) 반환\n" +
                                "3. **토큰 만료**: 401 에러 및 `ACCESS_TOKEN_EXPIRED` 메시지 수신 시 `/reissue` 호출\n" +
                                "4. **재발급**: `/reissue`는 쿠키의 `refresh`를 자동 사용\n" +
                                "5. **소셜 로그인**: 하이퍼링크를 이용해 /oauth2/authorization/{provider}` (kakao, google) 로 이동\n" +
                                "6. **소셜 로그인 성공 시**: `http://localhost:3000/login-success`로 리다이랙트 됨. 여기에서 `?access={AccessToken}`를 읽어 로컬 저장소에 보관하고 메인 페이지로 이동")
                        
                        .version("v1.0.0"))
                .components(components)
                .addSecurityItem(securityRequirement)
                
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("개발용 서버")
                ));
    }
    
}