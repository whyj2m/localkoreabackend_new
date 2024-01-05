package com.study.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.FileData;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, String>{
//	Optional<FileData> findByName(String uuid);
//	Optional<FileData> findByUui(String uuid);

}
