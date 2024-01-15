package com.example.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.memo.user.entity.UserEntity;
import com.example.memo.user.repository.UserRepository;

@Service
public class UserBO {
	@Autowired
	private UserRepository userRepository;
	
	//input: loginId  output:UserEntity(있거나 or null)
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	//input: 파라미터 4개 output: Integer id(pk)  <-insert실패하면 id값이 없으니까 Integer
	public Integer addUser(String loginId, String password, String name, String email) {
		UserEntity userEntity = userRepository.save(  //엔티티 만들자마자 파라미터로 넘김
				UserEntity.builder()
					.loginId(loginId)
					.password(password)
					.name(name)
					.email(email)
					.build()
				);
		return userEntity == null ? null : userEntity.getId();
	}
	
	
	//input: loginId, password   output:UserEntity(있거나 or null)
	public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
		return userRepository.findByLoginIdAndPassword(loginId, password);
	}

}