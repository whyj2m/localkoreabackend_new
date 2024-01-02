package com.study.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springboot.entity.category.LocationCategory;

@Repository
public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long>{

}
