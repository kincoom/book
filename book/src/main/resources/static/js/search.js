//부트 스트랩 폼에서 엔터키 입력시 submit action 안하도록 처리
$(document).ready(function() {
    $("#word").keydown(function(key) {
        if (key.keyCode == 13) {
            search(1,'Y');
            key.preventDefault();
        }
    });
});

//검색 및 페이지 이동
function search(page_no, searchByBtn){
	if($("#word").val()==""){
		alert("검색어를 입력하세요");
		return;
	}
	$("#page").val(page_no);
	$("#searchByBtn").val(searchByBtn);
	var params = $('#search_form').serializeObject();
	
	$.ajax({
		type : "POST",
		url : "/search/list",
		contentType: 'application/json',
		data : JSON.stringify(params),
		success : function(data){
			//meta정보로 페이징처리
			var meta = data.meta;
			//documents 정보로 리스트 보여주기
			var documents = data.documents;
			$("#searchResult").show();
			$("#listResult").html("");
			
			if(data.documents.length == "0"){
				$("#listResult").append("<tr><td colspan='2'>검색결과 없음</td></tr>");
			}else{
				$.each(documents, function (idx, val){
					$("#listResult").append("<tr>");
					$("#listResult").append("<td>" + val.title + "</td>");
					$("#listResult").append("<td><button class='btn btn-primary btn-sm' onclick ='detailView(\""+idx+"\")''>상세보기</button></td>");
					$("#listResult").append("<input type='hidden' id='bookDetail"+idx+"' value='"+JSON.stringify(val)+"'/>");
					$("#listResult").append("</tr>");
				});
			}

			//페이지 표시
			//현재 페이지
			var currentPage = $("#page").val();
			
			$("#pagination").html("");
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
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+prevPage+",\"N\")'>Previous</a></li>");	
			}
			//현재 페이지 그룹 보여주기
			for(var i= firstPage; i <= lastPage; i++ ){
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+i+",\"N\")'>"+i+"</a></li>");
			}
			//맨뒤 그룹이 아니면 다음 그룹 보여주기
			if(nextPage <= totalPage){
				$("#pagination").append("<li class='page-item'><a class='page-link' href='javascript:search("+nextPage+",\"N\")'>Next</a></li>");
			}
			
			
		},
		error : function(request,status,error){
			alert("조회 실패")
		}
	});
}

//책 상세정보보기
function detailView(idx){
	var bookInfo = JSON.parse($("#bookDetail"+idx).val());

	$("#bookImg").attr("src", bookInfo.thumbnail);
	$("#bookTitle").html(bookInfo.title);
	$("#bookIsbn").html(bookInfo.isbn);
	$("#bookAuthors").html(bookInfo.authors);
	$("#bookPublisher").html(bookInfo.publisher);
	$("#bookDatetime").html(bookInfo.datetime.substring(0,10));
	$("#bookPrice").html(bookInfo.price+"원");
	$("#bookSale_price").html(bookInfo.sale_price+"원");
	$("#bookContents").html(bookInfo.contents + "<a href='"+bookInfo.url+"' target='_sub'>...더보기</a>");
	
	$('#layerpop_book').modal("show");
}

//나의 검색기록 보기
function myHistory(){
	$.ajax({
		type : "POST",
		url : "/search/myHistory",
		contentType: 'application/json',
		datatype : 'json',
		success : function(data){
			
			$("#searchHistory").html("");
			
			$.each(data, function (idx, val){
				
				$("#searchHistory").append("<tr>");
				
				$("#searchHistory").append("<td>"+val.word+"</td>");
				$("#searchHistory").append("<td>"+val.searchTime.substring(0,10)+" " +val.searchTime.substring(11,19)+"</td>");
				
				$("#searchHistory").append("</tr>");
				
			});
			$('#layerpop_history').modal("show");
		},
		error : function(request,status,error){
			alert("조회 실패");
			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

//검색수 많은 상위 10개 보기
function getPapular(){
	$.ajax({
		type : "POST",
		url : "/search/papular",
		contentType: 'application/json',
		datatype : 'json',
		success : function(data){
			$("#searchPopular").html("");
			
			$.each(data, function (idx, val){
				
				$("#searchPopular").append("<tr>");
				
				$("#searchPopular").append("<td>"+val.word+"</td>");
				$("#searchPopular").append("<td>"+val.count+"</td>");
				
				$("#searchPopular").append("</tr>");
				
			});
			$('#layerpop_popular').modal("show");
			
		},
		error : function(request,status,error){
			alert("조회 실패");
			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

//로그아웃처리
function logout(){
	location.href="/logout";
}