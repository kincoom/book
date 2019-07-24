<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>책검색</title>
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

$(document).ready(function() {
    $("#word").keydown(function(key) {
        if (key.keyCode == 13) {
            search(1);
            key.preventDefault();
        }
    });
});

function search(page_no){
	if($("#word").val()==""){
		alert("검색어를 입력하세요");
		return;
	}
	$("#page").val(page_no);
	var params = $('#search_form').serializeObject();
	
	$.ajax({
		type : "POST",
		url : "/search/list",
		contentType: 'application/json',
		data : JSON.stringify(params),
		beforeSend : function(xhr)
        {   /*spring security  csrf값을 설정*/
            xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
        },
		success : function(data){
			//meta정보로 페이징처리
			var meta = data.meta;
			//documents 정보로 리스트 보여주기
			var documents = data.documents;
			$("#searchResult").show();
			$("#listResult").html("");
			$.each(documents, function (idx, val){
				$("#listResult").append("<tr>");
				$("#listResult").append("<td>" + val.title + "</td>");
				$("#listResult").append("<td>" + val.datetime.substring(0,10) + "</td>");
				$("#listResult").append("<td>" + val.publisher + "</td>");
				$("#listResult").append("</tr>");
			});

			//페이지 표시
			//현재 페이지
			var currentPage = $("#page").val();
			
			$("#pagination").html("");
			// 한페이지에 5개씩 보여줌
			var totalData = meta.pageable_count;		// 검색결과 수
			var countPerPage = 5;								// 그룹당 페이지

			
			var totalPage = Math.ceil(totalData/10);									//총 페이지수(api 리스트 10개씩 가져옴)
			var pageGroup = Math.ceil(currentPage/countPerPage);				//페이지 그룹

			var lastPage = pageGroup * countPerPage;	// 마지막 페이지 번호
			if(lastPage > totalPage){
				lastPage = totalPage;
			}
			var firstPage = countPerPage*(pageGroup-1) + 1;	//첫번째 페이지 번호

			var prevPage = firstPage-1;
			var nextPage = lastPage+1;

			//맨 앞 그룹이 아니면 크면 이전 그룹 보여주기
			if(prevPage > 0){
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+prevPage+")'>Previous</a></li>");	
			}
			//현재 페이지 그룹 보여주기
			for(var i= firstPage; i <= lastPage; i++ ){
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+i+")'>"+i+"</a></li>");
			}
			//맨뒤 그룹이 아니면 다음 그룹 보여주기
			if(nextPage <= totalPage){
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+nextPage+")'>Next</a></li>");
			}
			
			
		},
		error : function(request,status,error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}
</script>

</head>
<body>
<div class="container">
	<form id="search_form" class="form-inline">
		<select name="searchType" class="form-control">
			<option value="">전체</option>
			<option value="title">제목</option>
			<option value="isbn">ISBN</option>
			<option value="publisher">출판사</option>
			<option value="person">인명</option>
		</select>
		<input type="text" id="word" name="word" autocomplete="off" class="form-control">
		<button class="form-control" type="button" onclick="search(1)">검색</button>
         <input type="hidden" id="page" name="page" value=""/>
	</form>
</div>

<div class="container" id="searchResult" style="display:none;">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>제목</th>
				<th>출판일</th>
				<th>출판사</th>
			</tr>
		</thead>
		<tbody id="listResult">
		</tbody>
	</table>
	
	<div class="text-center">
	  <ul class="pagination" id="pagination">
	  </ul>
	</div>
</div>



</body>
</html>