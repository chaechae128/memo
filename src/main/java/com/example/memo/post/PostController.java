package com.example.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.memo.post.bo.PostBO;
import com.example.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 목록 화면
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/post-list-view")
	public String postListView(Model model, HttpSession session) {
		// 로그인 여부 확인-> 세션 확인
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {//권한이 없으면 글 목록 조회 x
			//비로그인이면 로그인 페이지로 이동
			return "redirect:/user/sign-in-view";
		}
		
		// DB 글 목록 조회(로그인 된 사람의) - mybatis
		List<Post> postList = postBO.selectPostListByUserId(userId);
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/postList");
		
		return "template/layout";
	}
	
	/**
	 * 글쓰기 화면
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView(Model model, HttpSession session) {
		
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId")int postId,
			Model model, 
			HttpSession session) {
		int userLoginId = (int) session.getAttribute("userId");
		//DB조회 - postId + userId   둘이 맞는지도 확인
		Post post = postBO.getPostByPostIdUserId(postId, userLoginId);
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetail");
		return "template/layout";
	}
}
