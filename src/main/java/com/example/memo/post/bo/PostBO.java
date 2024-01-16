package com.example.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.memo.post.domain.Post;
import com.example.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	@Autowired
	private PostMapper postMapper;
	
	//input: userId(로그인된 사람) output:List<post>
	public List<Post> selectPostListByUserId(int userId){
		return postMapper.getPostListByUserId(userId);
	}
}
