package com.kkj.book.entity;

import javax.persistence.*;

@Entity
public class MemberRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rno;
	
	private String roleName;

	public int getRno() {
		return rno;
	}

	public void setRno(int rno) {
		this.rno = rno;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
