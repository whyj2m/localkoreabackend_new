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
	
	public List<Board> findByTitleContaining(String keyword);
	
	// 카테고리별 조회
	public List<Board> findByCno(BoardCategory category);

	// 카테고리 숫자를 카테고리명으로 조회
//	public List<BoardCategory> findByLocationCno(BoardCategory category);
	 

}
