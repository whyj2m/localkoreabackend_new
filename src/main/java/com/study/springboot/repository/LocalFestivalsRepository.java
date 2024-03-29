package com.study.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.LocalFestivals;
import com.study.springboot.entity.Location;

@Repository
public interface LocalFestivalsRepository extends JpaRepository<LocalFestivals, Long>{

    @Query("SELECT fds FROM LocalFestivals fds WHERE fds.localNo = :localNo")
	List<LocalFestivals> findAllByLocalNo(@Param("localNo") Location localNo);

}
