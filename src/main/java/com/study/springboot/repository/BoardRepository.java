package com.study.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.LocalFoods;
import com.study.springboot.entity.Location;
import com.study.springboot.entity.Member;
import com.study.springboot.entity.category.BoardCategory;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.Heart;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	
	List<Board> findByTitleContaining(String keyword);
	
	// 카테고리별 조회
	List<Board> findByCno(BoardCategory category);
	 

}
