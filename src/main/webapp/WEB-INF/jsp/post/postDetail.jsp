<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-center align-items-center ">
	<div class="w-50">
		<h1>글상세</h1>
		
		<input type="text" id="subject" class="form-control" value="${post.subject}" >
		<textarea id="content" class="form-control" rows="5" placeholder="내용을 입력해주세요" >${post.content}</textarea>
		
		<%--이미지가 있을 경우에만 노출 --%>
		<c:if test="${not empty post.imagePath}">
			<div class="my-3">
				<img alt="업로드된 이미지" src="${post.imagePath}" width="300px">
			</div>
		</c:if>
		
		<div class="d-flex justify-content-end my-3">
			<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="deleteBtn"class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
			<div>
				<a  href="/post/post-list-view" class="btn btn-dark">목록</a>
				<button type="button" id="saveBtn"class="btn btn-info" data-post-id="${post.id}">수정</button>
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
		
		//수정 버튼 클릭 -> post insert
		$("#saveBtn").on('click', function(){
			//alert("saveBtn");
			let postId = $(this).data("post-id");
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let fileName = $("#file").val();//C:\fakepath\Microsoft_logo.svg.png
			//alert(postId);
			
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
			
			// 이미지를 업로드 할 때는 반드시 form 태그로 구성한다.
			let formData = new FormData();
			formData.append("postId", postId);
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $("#file")[0].files[0]); //꺼냈는데 없으면null
			
			$.ajax({
				//request
				type:"PUT"
				,url:"/post/update"
				,data:formData //String Type아님
				,enctype:"multipart/form-data" //파일 업로드를 위함 필수 설정
				,processData:false	//내가 보내는 파라미터는 스트링이 아니다! 파일 업로드를 위함 필수 설정
				,contentType:false  //파일 업로드를 위함 필수 설정
				
				//response
				,success:function(data){
					if(data.code == 200){
						alert("메모가 수정되었습니다.")
						location.reload(true);
					} else {
						alert(data.error_message);
					}
				}
				,error:function(request, status, error){
					alert("글을 수정하는데 실패했습니다.");
				}
			});
		});//saveBtn
		$("#deleteBtn").on('click', function(){
			//alert("deleteBtn");
			let postId = $(this).data("post-id");
			//alert(postId);
			$.ajax({
				type:"DELETE"
				,url:"/post/delete"
				,data:{"postId":postId}				
				,success:function(data){
					if(data.code == 200){
						alert("메모가 삭제되었습니다");
						location.href="/post/post-list-view";
					}else {
						alert(data.error_message);
					}
				}
				,error:function(request, status, error){
					alert("글을 삭제하는데 실패했습니다.");
				}
			});//ajax
		});//deleteBtn
		
	});
</script>