<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="/webjars/bootstrap/4.1.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/main.css">
<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script src="/webjars/bootstrap/4.1.1/dist/js/bootstrap.min.js"></script>
<script>
//form data json 형식으로 만들기
jQuery.fn.serializeObject = function() { 
	var obj = null; 
	try { 
		if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) { 
			var arr = this.serializeArray(); 
				if(arr){ obj = {}; 
					jQuery.each(arr, function() { 
					obj[this.name] = this.value; 
				}); 
			} 
		} 
	}catch(e) { 
		alert(e.message); 
	}finally {} 

	return obj; 
}

function test(){
	alert($("#test_input").val());
	$.ajax({
		type : "GET",
		url : "/test",
		datatype : "json",
		success : function(data){
			alert("success");
			alert(JSON.stringify(data));
			var result = data;
			$.each(result, function (idx, val){
				$("#hi").append("<div>"+val.id +"/" + val.memberId + "/" + val.pwd + "</div>");
			})
		},
		error : function(request,status,error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}
//회원가입 버튼 눌렀을때
 function reg_show(){
	 $('#reg_form').show();
}
//회원가입
function member_reg(){

	var params = $('#reg_form').serializeObject();
	
	$.ajax({
		type : "POST",
		url : "/member_reg",
		contentType: 'application/json',
		data : JSON.stringify(params),
		beforeSend : function(xhr)
        {   /*spring security  csrf값을 설정*/
            xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
        },
		success : function(data){
			$('#reg_form').hide();
			$("#hi").html("");
		},
		error : function(request,status,error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}
</script>
</head>
<body>
<div class="container login-container">
	<div class="row">
		<div class="col-md-6 login-form-1">
			<h3>로그인</h3>
			<form id="login-form" method="POST">
				<input type ="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<div class="form-group">
					<input type="text" name="username" class="form-control" placeholder="ID" value="" />
				</div>
				<div class="form-group">
					<input type="password" name="password" class="form-control" placeholder="비밀번호" value="" />
				</div>
				<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
					<div class="form-group">
			    		<font color="red">
					        <p>Your login attempt was not successful due to <br/>
					            ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</p>
					        <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session"/>
					    </font>
				    </div>
				</c:if>
				<div class="form-group">
					<input type="submit" class="btnSubmit" value="로그인" />
				</div>
				<div class="form-group">
					<a href="javascript:reg_show()" class="ForgetPwd">회원가입</a>
				</div>
			</form>
		</div>
	</div>
</div>

<form id="reg_form" name="reg_form" style="display:none;">
	<div>아이디 : <input type="text" name="memberId" id="reg_id"/></div>
	<div>비밀번호 : <input type="password" name="pwd" id="reg_pwd"/></div>
	<div><input type="button" onclick="member_reg()" value="가입하기"/></div>
</form>

<div>
	<input type="button" onclick="test()" value="전체회원"/>
	<div id ="hi"></div>
</div>
</body>
</html>