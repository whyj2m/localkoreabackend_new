
package com.study.springboot.entity;

import java.time.ZonedDateTime;

import com.study.springboot.entity.category.BoardCategory;
import com.study.springboot.entity.category.LocationCategory;
import com.study.springboot.entity.category.PlaceCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Board")
@Entity
public class Board {
	
	
//    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "boardSequence")

	@Id
    @SequenceGenerator (
            name = "boardSequence",
            sequenceName = "Board_SEQ",
            initialValue = 1,
            allocationSize = 1
    )
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;

    private String title;
    private String content;
    private String viewCnt;
    private ZonedDateTime regDate; 
	private ZonedDateTime updateDate; 
	@ManyToOne
	@JoinColumn(name="member_id", referencedColumnName = "id")
	private Member id;
	@ManyToOne
	@JoinColumn(name="board_cno", referencedColumnName = "cno")
	private BoardCategory cno; // 게시판 카데고리 1 - 관광지추천, 2 - 여행메이트, 3 - 공지사항
//	@ManyToOne
//	@JoinColumn(name="place_cno", referencedColumnName = "pcno")
//	private PlaceCategory pcno; // 관광지 카테고리번호
	@ManyToOne
	@JoinColumn(name="location_cno", referencedColumnName = "locno")
	private LocationCategory locno; // 지역 카테고리
	
	public void changeBoard(String title, String content) {
		this.title = title;
		this.content = content;
		this.updateDate = ZonedDateTime.now();
	}

}
