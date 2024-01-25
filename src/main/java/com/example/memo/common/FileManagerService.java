package com.example.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // spring bean .. autowired할 수 있음
public class FileManagerService {
	//이미지 업로드 , 삭제
	
	//실제 업로드 된 이미지가 저장될 경로(서버의 주소)
	
	//학원용//***imagese뒤에 /꼭 붙이기
	public static final String FILE_UPLOAD_PATH = "D:\\kimchaeyeon\\6_spring_project\\MEMO\\memo_workspace\\images/"; //상수  
	
	//집용
	//public static final String FILE_UPLOAD_PATH = "C:\\Users\\mouse\\OneDrive\\바탕 화면\\mega_백엔드\\spring_project_noetebook\\memo\\workspace\\images/"; //상수  
	
	//input:File(원본)multipart, userLoginId(폴더명)	output: 이미지 경로
	public String saveFile(String loginId, MultipartFile file) {
		//항상 사용자의 이름, 초로 구성됨 폴더를 만듦-> 이지미 파일이 겹치지 않게 하기 위함
		//폴더(디렉토리)생성
		//예: aaaa_1823478932/sun.png
		String directoryName = loginId + "_" + System.currentTimeMillis();  //파일명
		String filePath = FILE_UPLOAD_PATH + directoryName;  //"D:\\kimchaeyeon\\6_spring_project\\MEMO\\memo_workspace\\images/aaaa_1823478932/sun.png
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			//폴더 생성 실패시 이미지 경로 null로 리턴
			return null;
		} 
		
		//파일 업로드: byte 단위로 업로드
		try {
			byte[] bytes = file.getBytes();
			//★★★★★ 한글 이름 이미지는 올릴 수 없으므로 나중에 영문자로 바꿔서 올리기
			// file.getOriginalFilename() - 사용자가 올린 파일 이름의 원본
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename());
			Files.write(path, bytes); //실제 파일 업로드
		} catch (IOException e) {
			e.printStackTrace();
			return null; //이미지 업로드 실패시 null 리턴
		}
		
		//path Mapping 설정 해줘야함 
		//파일 업로드가 성공 했으면 웹 이미지 url path를 리턴
		//앞으로 이렇게 들어오면 그 주소로 연결 해줄거야~ (예언)
		// /images/aaaa_1823478932/sun.png   <- 이 주소가 바로 웹의 주소
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
		
	}
	
	//input: imagePath  output:x
	public void deleteFile(String imagePath) {
		// 주소에 겹치는 /images/  지운다.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images", ""));
		// 1) 삭제할 이미지 존재하는지 확인
		if(Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[파일매니저 삭제] 이미지 삭제 실패. path:{}", path.toString());
				return; 
			}
			
			//폴더(디렉토리)tkrwp
			path = path.getParent();
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					log.info("[파일매니저 삭제] 폴더 삭제 실패. path:{}", path.toString());
				}
			}
		}
		
	}

}
