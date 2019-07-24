package com.kkj.book.service;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kkj.book.entity.History;

@Service("bookService")
public class BookServiceImp implements BookService {
	
	static String key = "KakaoAK d81b0dd37cdce2ef9f6b6abf9b11602f";
	
	@Override
	public Map<String, Object> bookListApi(History history) throws Exception {
		//현재 사용자 ID
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		
		String api_url = "https://dapi.kakao.com/v3/search/book";
		
		String searchWord = history.getWord();
		
		String param = "?query="+URLEncoder.encode(searchWord,"UTF-8")
		+"&page="+history.getPage()
		+"&target="+history.getSearchType();
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
		headers.set("Authorization", key);
		
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers); 
		URI url=URI.create(api_url+param);
		
		ResponseEntity<String> response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		JSONParser jsonParser = new JSONParser(); 
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
		
		return jsonObject;
	}
}
