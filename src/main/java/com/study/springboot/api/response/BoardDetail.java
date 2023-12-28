package com.study.springboot.api.response;

import java.time.ZonedDateTime;

import com.study.springboot.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardDetail {
	
	private Long bno;
	private String title;
    private String content;
    private String viewCnt;
    private ZonedDateTime regDate; 
	private ZonedDateTime updateDate; 
	private Member id;

}
