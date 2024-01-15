package com.study.springboot.api.response;

import java.time.ZonedDateTime;

import com.study.springboot.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardList {
	
	private Long bno; // 게시글 번호
	private String title; // 제목
    private String content; // 내용
    private Long viewCnt; // 조회수
    private ZonedDateTime regDate; // 작성일시
	private ZonedDateTime updateDate; // 수정일시
	private Member id; // 회원 id
	private Long boardCno; // 카테고리 번호
	private Long locationCno; // 지역 카테고리 번호
	private String location;
	
	@Getter
	@Builder
	@AllArgsConstructor
	public static class BoardCategory{
		private Long cno;
		private String cname;
	}
}
