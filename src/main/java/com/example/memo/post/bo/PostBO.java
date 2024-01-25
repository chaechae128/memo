package com.example.memo.post.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;// 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.memo.common.FileManagerService;
import com.example.memo.post.domain.Post;
import com.example.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j  //이걸 쓰면 private Logger logger = LoggerFactory.getLogger(PostBO.class); 이걸 안 써도됨
@Service
public class PostBO {
	//private Logger logger = LoggerFactory.getLogger(PostBO.class);
	//private Logger logger = LoggerFactory.getLogger(this.getClass()); //지금 이 클래스에 대해서 로깅을 할 것이다
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
	
	//input: postId, userId	output:Post
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
	}
	
	//input:params  output:x
	public void updatePostByPostId(int userId, int postId, String userLoginId, String subject, String content, MultipartFile file) {
		//기존 글을 가져온다.(1. 이미지 교체시 삭제하기 위해 , 2. 업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if(post == null) {//npa를 막기 위해 실무에서는 null 검사를 많이 함!!! 이상한 일이지만 null이 발생할 수도 있음
			//System.out.println();   이런 코드는 실무에서 절대로 쓰면 안 됨!!
			log.info("[글 수정] post is null. postId:{}, userId:{}", postId, userId); //개인프로젝트 시 많이 찍어두는 것이 좋음
			return;
		}
		//파일이 있다면
		// 1) 새 이미지를 업로드 한다.(서버에)
		// 2) 1번 단계가 성공하면 기존 이미지 제거(기존 이미지가 있다면)
		String imagePath = null;
		if(file != null) {
			//업로드
			imagePath = fileManagerService.saveFile(userLoginId, file);
					
			//업로드 성공 시 기존 이미지가 있으면 제거(메모리 낭비 최소화)
			if(imagePath != null && post.getImagePath() != null) {
				//업로드 성공하고 기존 이미지 있으면 서버의 파일 제거
				fileManagerService.deleteFile(post.getImagePath()); //imagePath를 파라미터로 넘기면 방금 받아온 이미지를 삭제하는 것이기 떄문에 주의!
			}
		}
		//db 업데이트
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
		
	}
	
	//input: postId, userId output:x
	public void deletePostByPostIdUserId(int userId, int postId) {
		// 기존 글이 있는지 확인(삭제할 대상이 있는가, 이미지가 있는가)
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if(post == null) {
			log.info("[글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		//db에서 post 삭제
		int deleteRowCount = postMapper.deletePostByPostId(postId);

		//이미지가 존재하면 삭제(DB 삭제도 성공 했을 시만)
		if(deleteRowCount > 0 && post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}

		
		
	}
}
