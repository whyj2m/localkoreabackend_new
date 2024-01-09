package com.study.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.BoardReply;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

//	 Optional<BoardReply> findByBno(Long bno);
	 // 첫 번째 댓글을 생성 시간순으로 찾는 쿼리 메서드
//	 List<BoardReply> findByBno(Long bno);
}

