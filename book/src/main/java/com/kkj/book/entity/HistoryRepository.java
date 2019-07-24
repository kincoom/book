package com.kkj.book.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>{
	
	List<History> findByMemberIdOrderBySearchTimeDesc(@Param("member_id") String member_id);
	
	@Query(value = "SELECT word, count(*) as count, ROWNUM() as rnum FROM history a GROUP BY a.word ORDER BY count desc limit 10", nativeQuery = true)
	List<Object[]> getPopularSearch();
}
