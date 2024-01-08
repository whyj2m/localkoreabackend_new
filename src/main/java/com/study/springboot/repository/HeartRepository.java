package com.study.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.Heart;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
	List<Heart> findById_Id(String id);

}
