package com.kkj.book.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkj.book.entity.History;
import com.kkj.book.entity.HistoryRepository;
import com.kkj.book.entity.Member;
import com.kkj.book.entity.MemberRepository;
import com.kkj.book.entity.MemberRole;
import com.kkj.book.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private BookService bookService;
	@Autowired
	private HistoryRepository historyRepository;
	
	// 최초 로그인 페이지 뷰 이동
	@RequestMapping("/login")
	public String index(Model model) {
		return "login";
	}
	
	// 검색페이지 뷰 이동
	@RequestMapping("/search")
	public String search(Model model) {
		return "search";
	}
	
	// 회원가입 프로세스
	@ResponseBody
	@RequestMapping(value="/member_reg", method=RequestMethod.POST)
	public int member_reg(@RequestBody Member member) {
		int result = 0;
				
		//회원가입전 동일 아이디 있는지 체크
		Member check = memberRepository.findByMemberId(member.getMemberId());
		if(check != null) {
			result = -100;
			return result;
		}
		
		//spring security를 이용하여 가입 처리
		MemberRole role = new MemberRole();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		member.setPwd(passwordEncoder.encode(member.getPwd()));
		role.setRoleName("BASIC");
		member.setRoles(Arrays.asList(role));
		memberRepository.save(member);
		result = 1;
		
		return result;
	}
	
	//카카오 책 검색 API를 통한 리스트 JSON으로 처리
	@ResponseBody
	@RequestMapping(value="/search/list", method=RequestMethod.POST)
	public Map<String,Object> searchBook(@RequestBody History history) throws Exception {
		
		//API를 이용한 검색
		Map<String,Object> result =  bookService.bookListApi(history);
		
		//현재 로그인한 사용자 아이디를 history테이블에 같이 넣어줌
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		history.setMemberId(user.getUsername());
		

		//검색버튼을 통해 검색했을 때만 히스토리 테이블 INSERT(페이지 이동시마다 검색내역 insert 방지)
		if(history.getSearchByBtn().equals("Y")) {
			historyRepository.save(history);
		}
		
		return result;
	}
	
	//나의 검색 내역 조회
	@ResponseBody
	@RequestMapping(value="/search/myHistory", method=RequestMethod.POST)
	public List<History> myHistory() throws Exception {
		
		//현재 로그인한 ID 조건으로 테이블 검색
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String memberId = user.getUsername();
		
		List<History> result = historyRepository.findByMemberIdOrderBySearchTimeDesc(memberId);
		
		return result;
	}
	
	//검색수 많은 검색어 상위 10개 불러오기
	@ResponseBody
	@RequestMapping(value="/search/papular", method=RequestMethod.POST)
	public List<Map<String,String>> popular() throws Exception {
				
		List<Object[]> object_result = historyRepository.getPopularSearch();
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;

		if(object_result != null && !object_result.isEmpty()){
			for (Object[] object : object_result) {
				map = new HashMap<String,String>();
				//group by 검색어
				map.put("word", String.valueOf(object[0]));
				//group by 카운트
				map.put("count", String.valueOf(object[1]));
				
				result.add(map);
			}
		}
		
		return result;
	}

}
