package com.study.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{


}
