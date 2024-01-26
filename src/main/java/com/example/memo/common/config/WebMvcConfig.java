package com.example.memo.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.memo.common.FileManagerService;
import com.example.memo.interceptor.PermissionInterceptor;

@Configuration //설정을 위한 spring bean
public class WebMvcConfig implements WebMvcConfigurer{
	@Autowired
	private PermissionInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(interceptor)
		.addPathPatterns("/**")//모든주소에 대해 intercept할거다
		.excludePathPatterns("/static/**", "/error", "/user/sign-out")//제외하고싶은 주소
		;
	}
	
	//웹 이미지 path와 서버에 업로드 된 실제 이미지와 매핑 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/images/**") //web path
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 실제 이미지 파일 위치
		// macos 2개 window 3개
	}
}
