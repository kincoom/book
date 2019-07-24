package com.kkj.book.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kkj.book.entity.History;

@Repository
public interface BookService {
	public Map<String,Object> bookListApi(History history) throws Exception;
}
