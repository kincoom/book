package com.kkj.book.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class History {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@Column
	String word;
	
	@Column
	String memberId;
	
	@Column
	@CreationTimestamp
	LocalDateTime searchTime;
	
	@Transient
	String searchType;
	
	@Transient
	int page;
	
	@Transient
	int count;
	
	@Transient
	String searchByBtn;
	
	public History() {
	}
	
	public History(String word, String memberId) {
		this.word = word;
		this.memberId = memberId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public LocalDateTime getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(LocalDateTime searchTime) {
		this.searchTime = searchTime;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSearchByBtn() {
		return searchByBtn;
	}

	public void setSearchByBtn(String searchByBtn) {
		this.searchByBtn = searchByBtn;
	}
	
}
