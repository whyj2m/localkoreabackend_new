package com.study.springboot.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApi {
	
	@Autowired
	private BoardService boardService;
	
	// 게시글 전체 조회
//	@GetMapping("/board")
//	public List<BoardList> getBoardList(){
//		return boardService.findAllBoard();
//	}
	
	// 카테고리별 api
//	@GetMapping("")
//	public List<BoardCategory> getBoardCategories(){
//		return boardService.fin
//	}
	
	// 관광지 추천 게시글 조회
	@GetMapping("/board/tourisSpot")
	@CrossOrigin
	public List<BoardList> getBoardList(){
		return boardService.findByTourisSpot();
	}
	
	// 관광지 추천 bno별 상세 조회
	@GetMapping("/board/tourisSpot/{bno}")
	@CrossOrigin
	public BoardDetail getBoardDetail(@PathVariable Long bno) {
		return boardService.findByBno(bno);
	}
	
	// 여행메이트 게시글 조회
	@GetMapping("/board/company")
	@CrossOrigin
	public List<BoardList> getCompanyList(){
		return boardService.findByCompany();
	}

	// 게시글 작성
	@PostMapping("/board/boardWrite")
	@CrossOrigin
	public ResponseEntity<String> inserBoard(
			@RequestBody CreateAndEditBoardRequest request
			){
		try {
			boardService.insertBoard(request);
			return ResponseEntity.ok("Data Input Completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
		}
	}
	
	// 게시글 수정
	@PutMapping("/board/edit/{bno}")
	@CrossOrigin
	public void editBoard(
			@PathVariable(name = "bno") Long bno,
			@RequestBody CreateAndEditBoardRequest request
			) {
		boardService.editBoard(bno, request);
	}
	
	// 글 삭제
	@DeleteMapping("/board/delete/{bno}")
	@CrossOrigin
	public void deleteBoard(
			@PathVariable(name = "bno") Long bno
			) {
		boardService.deleteBoard(bno);
	}	
	
	//
//	@GetMapping("/board/{bno}")
//	public BoardDetail getBoard(
//			@PathVariable(name = "bno") Long bno
//			) {
//		return boardService.findById(bno);
//	}
//	
//	@PutMapping("/board/{bno}")
//	public void editBoard(
//			@PathVariable(name = "bno") Long bno,
//			@RequestBody CreateAndEditBoardRequest request
//			) {
//		boardService.editBoard(bno, request);
//	}
//	
//	@DeleteMapping("/board/{bno}")
//	public void deleteBoard(
//			@PathVariable(name = "bno") Long bno
//			) {
//		boardService.deleteBoard(bno);
//	}
}
