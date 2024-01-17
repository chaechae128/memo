<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center align-items-center ">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력해주세요">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력해주세요"></textarea>
		
		<div class="d-flex justify-content-end my-3">
			<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn"class="btn btn-dark">목록</button>
			<div>
				<button type="button" id="clearBtn"class="btn btn-secondary">모두지우기</button>
				<button type="button" id="saveBtn"class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function(){
		//목록 버튼 클릭-> 목록 화면 이동
		$("#postListBtn").on('click', function(){
			location.href = "/post/post-list-view";
		});//postListBtn
		
		//모두 지우기 버튼 클릭
		$("#clearBtn").on('click', function(){
			//alert("clearBtn");
			$("#subject").val("");
			$("#content").val("");
			
		});//clearBtn
		
		//저장 버튼 클릭 -> post insert
		$("#saveBtn").on('click', function(){
			//alert("saveBtn");
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let fileName = $("#file").val();//C:\fakepath\Microsoft_logo.svg.png
			//alert(fileName);
			
			//validation
			if(!subject) {
				alert("제목을 입력하세요");
				return;
			}
			if(!content) {
				alert("내용을 입력하세요");
				return;
			}
			
			//파일이 업로드 된 경우에만 확장자 체크
			if(fileName) {
				//alert("파일이 있다.");
				//C:\fakepath\Microsoft_logo.svg.png
				//확장자만 뽑은 후 소문자로 변경해서 검사한다. pop- 배열의 마지막만 뽑음
				let extentsion = fileName.split(".").pop().toLowerCase();
				//alert(extentsion);
				
				if($.inArray(extentsion, ['jpg', 'png', 'gif', 'jpeg']) == -1){ //inArray이 배열에 포함되어있는가 없으면 -1
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$("#file").val(""); //이미지 파일 아닌 것을 비운다.
					return;
				}
			}
			
			// form 태그를 js에서 만든다.
			// 이미지를 업로드 할 때에는 반드시 form 태그가 있어야한다.
			let formData = new FormData();
			formData.append("subject", subject);//form 태그 안쪽에 내용을 만든다 key는 name속성과 같다. Request Parameter명
			formData.append("content", content); 
			formData.append("file", $("#file")[0].files[0]); //파일 여러개 올릴 때는 multi로 바꿔야함
			
			//나중에 이미지 파일도 추가
			
			//AJAX
			$.ajax({
				//request
				type: "POST"
				,url: "/post/create"
				,data: formData // form을 통째로 보냄
				,enctype:"multipart/form-data" //파일 업로드를 위한 필수 설정  * 특히 이미지 있을 떄 반드시 필요
				,processData:false //파일 업로드를 위한 필수 설정
				,contentType:false //파일 업로드를 위한 필수 설정  dataparameter를 보낼 떄 원랜 String 으로 보내는데 난 객체로 보내겠다.
				//form 태그를 만들고 하면 form 에 enctype속성 추가해야됨
				
				//response
				,success:function(data){
					if(data.code == 200){//성공시
						alert("메모가 저장되었습니다.");
						location.href="/post/post-list-view";
					}
					else {
						alert(data.error_message);
					}
				}
				,error:function(request, status, error){
					alert("글을 저장하는데 실패했습니다.");
				}
				
			});//ajax
			
			
			
		});//saveBtn
	});
</script>