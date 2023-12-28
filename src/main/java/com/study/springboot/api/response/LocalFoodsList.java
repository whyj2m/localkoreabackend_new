package com.study.springboot.api.response;

import java.time.ZonedDateTime;

import com.study.springboot.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocalFoodsList {
	
	private Long foodNo;
	private String name;
	private String content;
	private String viewCnt;
	private Location localNo;

}
