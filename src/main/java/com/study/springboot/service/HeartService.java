package com.study.springboot.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.springboot.api.request.CreateAndEditHeartRequest;
import com.study.springboot.api.request.CreateAndEditLocalFoodsRequest;
import com.study.springboot.api.request.CreateAndEditMemberRequest;
import com.study.springboot.api.response.HeartDetail;
import com.study.springboot.api.response.HeartList;
import com.study.springboot.api.response.LocalFoodsDetail;
import com.study.springboot.api.response.LocalFoodsList;
import com.study.springboot.api.response.MemberDetail;
import com.study.springboot.api.response.MemberList;
import com.study.springboot.entity.Heart;
import com.study.springboot.entity.LocalFoods;
import com.study.springboot.entity.Member;
import com.study.springboot.repository.HeartRepository;
import com.study.springboot.repository.LocalFoodsRepository;
import com.study.springboot.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeartService {
	
	@Autowired
	private HeartRepository heartRepository;
	
	@Transactional
	public Heart insertHeart(CreateAndEditHeartRequest request) {
		Heart heart = Heart.builder()
							.id(request.getId())
							.placeNo(request.getPlaceNo())
							.build();
		heartRepository.save(heart);
		
		return heart;
	}
	
	@Transactional
	public List<HeartList> findAllHearts(){
		List<Heart> hearts = heartRepository.findAll();
		return hearts.stream().map((heart) -> HeartList.builder()
							.heartNo(heart.getHeartNo())
							.id(heart.getId())
							.placeNo(heart.getPlaceNo())
							.build()
							).toList();
		
	}
	
	@Transactional
	public HeartDetail findById(Long heartNo) {
		Heart hearts = heartRepository.findById(heartNo).orElseThrow();
		
		return HeartDetail.builder()
								.heartNo(hearts.getHeartNo())
								.id(hearts.getId())
								.placeNo(hearts.getPlaceNo())
								.build();
	}
	
//	@Transactional
//	public void editHeart(Long heartNo, CreateAndEditLocalFoodsRequest request) {
//		Heart heart = heartRepository.findById(heartNo)
//				.orElseThrow(() -> new RuntimeException("Known LocalFood"));
//		heart.changeH(request.getName(), request.getContent(), request.getViewCnt());
//		foodsRepository.save(foods);
//		
//	}
	
	@Transactional
	public void deleteHeart(Long heartNo) {
		Heart heart = heartRepository.findById(heartNo).orElseThrow();
		heartRepository.delete(heart);
	}

}
