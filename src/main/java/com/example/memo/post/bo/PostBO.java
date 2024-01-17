package com.example.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.memo.common.FileManagerService;
import com.example.memo.post.domain.Post;
import com.example.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	//input: userId(로그인된 사람) output:List<post>
	public List<Post> selectPostListByUserId(int userId){
		return postMapper.getPostListByUserId(userId);
	}
	
	//input: params  output:x
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		//BO에서 MultipartFile를 imagePath로 변경해야함
		
		String imagePath = null;
		//업로드 할 이미지가 있을 때 업로드
		if(file != null) {
			//파일 넘겨줄테니 주소 내놔
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		
		postMapper.insertPost(userId, userLoginId, subject, content, imagePath);
	}
	
}
