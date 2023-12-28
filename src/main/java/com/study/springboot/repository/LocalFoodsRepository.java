package com.study.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.springboot.api.response.LocalFoodsList;
import com.study.springboot.entity.LocalFoods;
import com.study.springboot.entity.Location;
import com.study.springboot.entity.Member;

@Repository
public interface LocalFoodsRepository extends JpaRepository<LocalFoods, Long>{

    @Query("SELECT fds FROM LocalFoods fds WHERE fds.localNo = :localNo")
	List<LocalFoods> findAllByLocalNo(@Param("localNo") Location localNo);


}
