package com.study.springboot.api.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationList {
	
	private Long localNo;
	private String name;
    private String content;
    private String poplation;
    private String area;
    private String flower;

}
