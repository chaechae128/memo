package com.example.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
	/**
	 * 회원가입 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-up-view")
	public String signUpView(Model model) {
		model.addAttribute("viewName", "user/signUp");
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-in-view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "/user/signIn");
		return "template/layout";
	}
	
	@RequestMapping("/sign-out") //get, post 둘다일 때
	public String signOut(HttpSession session) {
		//세션에 있는 내용을 모두 비운다.
		session.removeAttribute("userId");
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		
		// redirect방식으로 로그인 화면으로 이동 (이미 있는 메소드)
		return "redirect:/user/sign-in-view";
	}
}
