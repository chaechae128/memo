package com.example.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.memo.post.domain.Post;

@Mapper
public interface PostMapper {
	
	//input : x output:List<Map>
	public List<Map<String, Object>> selectPostlist();
	
	//input: userId output:List<Post>
	public List<Post> getPostListByUserId(int userId);
	
	public void insertPost(
			@Param("userId")int userId,
			@Param("userLoginId") String userLoginId,
			@Param("subject")String subject, 
			@Param("content")String content, 
			@Param("imagePath")String imagePath);
	
	
	public Post selectPostByPostIdUserId(
			@Param("postId")int postId, 
			@Param("userId")int userId);
	
	public void updatePostByPostId(
			@Param("postId")int postId,
			@Param("subject")String subject, 
			@Param("content")String content, 
			@Param("imagePath")String imagePath);
	
	public void deletePostByPostId(int postId);
}
