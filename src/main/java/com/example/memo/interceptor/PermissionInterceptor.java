package com.example.memo.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component //spring bean
public class PermissionInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		//요청 URL Path를 꺼낸다. uri > url 개념...?
		String uri = request.getRequestURI();
		log.info("[********** preHandle] uri:{}", uri);
		
		//로그인 여부
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		
		//비로그인 && /post 접근일 때 => 로그인 페이지로 이동, 컨트롤러 수행 방지
		if(userId == null && uri.startsWith("/post")) {
			response.sendRedirect("/user/sign-in-view");
			return false; //원래 요청에 대해서 컨트롤러 수행 X
		}
		//로그아웃 조심!!!!!!!!!!!!!
		//로그인 && /user => 글목록 페이지로 이동, 컨트롤러 수행 방지
		if (userId != null && uri.startsWith("/user")) {
			response.sendRedirect("/post/post-list-view");
			return false;
		}
		
		return true; // controller 수행 원래 요청한 것을 수행을 허락
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView mav) { // mvc패턴의 모델과 뷰 아직 jsp안 갔다는 의미  이 타이밍은 응답으로 내리되 jsp는 아직
		
		//view객체가 있다는 건 아직 jsp가 html로 변환되기 전
		log.info("[########### postHandle]");
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		//jsp가 html로 최종 변환된 후
		log.info("[@@@@@@@@@ afterCompletion]");
	}
}
