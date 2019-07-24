package com.kkj.book.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kkj.book.entity.MemberRepository;
import com.kkj.book.entity.SecurityMember;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	MemberRepository memberRepository;
	
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		return
				Optional.ofNullable(memberRepository.findByMemberId(memberId))
				.filter(m -> m!= null)
				.map(m -> new SecurityMember(m)).get();
	}
}
