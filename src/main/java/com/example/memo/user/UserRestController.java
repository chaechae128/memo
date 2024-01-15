package com.example.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.common.EncryptUtils;
import com.example.memo.user.bo.UserBO;
import com.example.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	@Autowired
	private UserBO userBO;
	
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId){
		
		//DB조회 - select
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		Map<String, Object> result = new HashMap<>();
		
		//응답
		if(user != null) { // 중복
			result.put("code", 200);
			result.put("is_duplicated_id", true);
		} else { //중복xs
			result.put("code", 200);
			result.put("is_duplicated_id", false);
		}
		
		return result;
	}
	/**
	 * 회원가입API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email){
		
		// md5 알고리즘 -> password hashing  <- 보안 조금 취약 그러니 개인프로젝트할  때는 다른 알고리즘
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);  //<-결과로 해싱된 password
		
		//DB insert
		Integer userId = userBO.addUser(loginId, hashedPassword, name, email);
		
		
		//응답
		Map<String, Object> result =  new HashMap<>();
		if(userId != null) { //userId가 null이 아니면 성공
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}
		
		
		return result;
	}
	
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request){
		
		//비밀번호 hashing - md5
		String hashedPassword = EncryptUtils.md5(password);
				
		// DB조회 (loginId, 해싱된 비밀번호) => UserEntity 
		UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if(user != null) { //로그인 성공
			//로그인처리
			//로그인 정보를 세션에 담는다.(사용자마다) = 브라우저마다 보통 기본 시간:30분
			HttpSession session = request.getSession(); //jssesionId가 통과
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "성공");
		}else { //로그인 불가
			result.put("code", 300); //권한이 없는건 300대
			result.put("error_message", "존재하지 않는 사용자 입니다.");
		}
		
		return result;
	}
	
}
