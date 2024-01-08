package com.study.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.entity.FileData;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long>{

	List<FileData> findByBoardBno(Long boardBno);
	List<FileData> findByUuid(String uuid);
//	List<MultipartFile> getImageFilesByBno(Long boardBno);
	
	
}
