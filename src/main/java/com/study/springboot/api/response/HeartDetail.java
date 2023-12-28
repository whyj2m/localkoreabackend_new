package com.study.springboot.api.response;

import com.study.springboot.entity.LocalPlaces;
import com.study.springboot.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HeartDetail {
	
	private Long heartNo;
	private Member id;
	private LocalPlaces placeNo;

}
