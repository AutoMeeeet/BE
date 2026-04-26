<<<<<<< HEAD
// Security 적용 시 제거
package com.teamproject.meeting.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsMvcConfig implements WebMvcConfigurer {
	
	@Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000");
    }
}
=======
//// Security 적용 시 제거
//package com.teamproject.meeting.config;
//
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class CorsMvcConfig implements WebMvcConfigurer {
//	
//	@Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        
//        corsRegistry.addMapping("/**")
//        		.exposedHeaders("Set-Cookie")
//                .allowedOrigins("http://localhost:3000");
//    }
//}
>>>>>>> origin/dev
