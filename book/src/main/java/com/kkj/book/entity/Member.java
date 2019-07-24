package com.kkj.book.entity;

import java.util.List;

import javax.persistence.*;

@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	int id; 
	
	@Column
	String memberId;
	@Column
	String pwd;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="uid")
	private List<MemberRole> roles;
	
	public Member()	{}
	
	public Member(String memberId, String pwd) {
		this.memberId = memberId;
		this.pwd = pwd;
	}

	
	public int getId() {
		return id; 
	}
	  
	public void setId(int id) {
		this.id = id; 
	}	 

	public String getMemberId() {
		return memberId;
	}

	public void setMember_id(String memberId) {
		this.memberId = memberId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public List<MemberRole> getRoles() {
		return roles;
	}

	public void setRoles(List<MemberRole> roles) {
		this.roles = roles;
	}
	
	
}
