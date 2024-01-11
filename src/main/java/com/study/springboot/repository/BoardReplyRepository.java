package com.study.springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.Board;
import com.study.springboot.entity.BoardReply;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    List<BoardReply> findByBoard_Bno(Long bno); // 메소드 이름 변경
}
