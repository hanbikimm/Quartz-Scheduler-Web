package com.hansol.common.config;

import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//      // Spring Security를 사용하지 않는 경우
//		registry.addMapping("/**")
//				.allowedOrigins("http://localhost:8080")
//				.allowedHeaders("Authorization", "Content-Type", "Content-Length", "Cache-Control")
//				.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE")
//				.allowCredentials(false)
//				.maxAge(3600);
//    }
}