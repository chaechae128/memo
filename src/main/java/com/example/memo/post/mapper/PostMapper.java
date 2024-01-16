package com.example.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.memo.post.domain.Post;

@Mapper
public interface PostMapper {
	
	//input : x output:List<Map>
	public List<Map<String, Object>> selectPostlist();
	
	//input: userId output:List<Post>
	public List<Post> getPostListByUserId(int userId);
}
