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

//spring security ajax 권한 추가 
var token = $("meta[name='_csrf']").attr("content"); 
var header = $("meta[name='_csrf_header']").attr("content"); 
$(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });