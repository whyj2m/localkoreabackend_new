package com.study.springboot.api.request;

import java.time.ZonedDateTime;

import com.study.springboot.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndEditBoardRequest {
	
	private String title;
    private String content;
    private String viewCnt;
    private ZonedDateTime regDate; 
	private ZonedDateTime updateDate; 
	private Member id;

}
