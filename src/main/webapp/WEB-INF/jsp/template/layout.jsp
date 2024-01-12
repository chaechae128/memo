<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MEMO 게시판</title>
<%-- bootsrtrap --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-Fy6S3B9q64WdZWQUiU+q4/2Lc9npb8tCaSX9FK7E8HnRr0Jz8D6OP9dO5Vg3Q9ct" crossorigin="anonymous"></script>

<%--내가 만든 스타일시트 --%>
<link rel="stylesheet" type="text/css" href="/static/css/style.css">
</head>
<body>
	<div id="wrap">
            <header>
                <jsp:include page="../include/header.jsp" />
            </header>
            <section class="contents my-5">
      			<jsp:include page="../${viewName}.jsp" />
            </section>
            <footer>
                <jsp:include page="../include/footer.jsp" />
            </footer>
        </div>

        <script>
            $(document).ready(function(){
                //alert("히히");

                $("#IdCheckBtn").on('click', function(){
                    let id = $("#id").val().trim();
                    if(!id) {
                        alert("id를 입력하세요");
                        return false;
                    }

                    $.ajax({
                        type:"POST"
                        ,url:"/user/is-duplicated-id"
                        ,data: {"id":id}

                        //response
                        ,success:function(data){
                            if(data.is_duplication) {
                                $("#duplicationText").removeClass("d-none");
                                $("#availableText").addClass("d-none");
                            } else {
                                $("#availableText").removeClass("d-none");
                                $("#duplicationText").addClass("d-none");
                            }
                        }
                        ,error:function(request, status, error){
                            alert("id 중복확인에 실패했습니다.");
                        }
                        
                    });

                }); //IdCheckBtn


                $("#signUpBtn").on('click', function(){
                    //validation
                    let id = $("#id").val().trim();
                    let password = $("#password").val().trim();
                    let name = $("#name").val().trim();
                    let email = $("#email").val().trim();

                    if(!id) {
                        alert("id를 입력하세요");
                        return false;
                    }
                    if(!password) {
                        alert("비밀번호를 입력하세요");
                        return false;
                    }
                    if(!name) {
                        alert("이름을 입력하세요");
                        return false;
                    }
                    if(!email) {
                        alert("이메일을 입력하세요");
                        return false;
                    }

                    $.ajax({
                        type:"POST"
                        ,url:"/user/sign_up"
                        ,data:{"id":id, "password":password, "name":name, "email":email}

                        ,success:function(data){
                            if(data.code == 200) {
                                //회원가입 성공 시 로그인 패이지로
                                location.href="/user/sign-in-view"
                            }
                        }
                        ,error:function(request, status, error){
                            alert(data.error_message);
                        }
                    })
                }); //signUpBtn

            }); //ready

        </script>
</body>
</html>