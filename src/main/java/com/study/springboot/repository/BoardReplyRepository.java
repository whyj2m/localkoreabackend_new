package com.study.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.BoardReply;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

//	Optional<BoardReply> findByBno(Long boardBno);
//	Optional<BoardReply> findByBno(Long bno);
	
}

