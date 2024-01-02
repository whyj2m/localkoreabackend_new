package com.study.springboot.api.request;

import java.time.ZonedDateTime;
import java.util.List;

import com.study.springboot.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndEditBoardRequest {
	
	private String title;
    private String content;
    private String viewCnt;
    private ZonedDateTime regDate; 
	private ZonedDateTime updateDate; 
	private Member id;
	private Long boardCno; // 카테고리 번호
	private Long locationCno; // 지역 카테고리 번호
	private String location; // 지역 이름
	

}
