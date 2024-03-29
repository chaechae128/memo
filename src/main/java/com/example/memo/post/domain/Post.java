package com.example.memo.post.domain;


import java.util.Date;

import lombok.Data;
import lombok.ToString;

@ToString
@Data //getter/ setter
public class Post {

	private int id;
	private int userId;
	private String subject;
	private String content;
	private String imagePath;
	private Date createdAt;
	private Date updatedAt;
	
	
	
}
