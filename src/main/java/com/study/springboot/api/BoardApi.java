package com.study.springboot.api;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.FileData;
import com.study.springboot.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
//		return boardService.find
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
		
		BoardDetail board = boardService.findByBno(bno);
		
		boardService.viewCount(bno); // 조회수 1씩 증가
		
		if(board.getViewCnt() == null) {  // viewCnt가 null이면 0으로 설정
			board.setViewCnt(0L);
		}
		
		return boardService.findByBno(bno);
	}
	
	// bno별 이미지 조회	
	@GetMapping("/api/images/{bno}")
    public ResponseEntity<List<FileData>> getImagesByBno(@PathVariable Long bno) {
        List<FileData> fileDataList = boardService.getFileDataByBno(bno);

        if (fileDataList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(fileDataList, HttpStatus.OK);
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
	public ResponseEntity<String> insertBoardWithFile(
	        @RequestParam(value = "title") String title,
	        @RequestParam(value = "content") String content,
	        @RequestParam(value = "boardCno") Long boardCno,
	        @RequestParam(value = "locationCno") Long locationCno,
	        @RequestParam(value = "location") String location,
	        @RequestPart("files") List<MultipartFile> files
	) {
	    try {
	        CreateAndEditBoardRequest request = new CreateAndEditBoardRequest();
	        request.setTitle(title);
	        request.setContent(content);
	        request.setBoardCno(boardCno);
	        request.setLocationCno(locationCno);
	        request.setLocation(location);

	        Board board = boardService.insertBoard(request, files); // 게시글과 파일들을 함께 처리

	        return ResponseEntity.ok("백엔드 파일 업로드 성공");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
	    }
	}
//	@PostMapping(value = "/board/boardWrite", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@PostMapping(value = "/board/boardWrite", consumes = "multipart/form-data")
//	@CrossOrigin
//	public ResponseEntity<String> insertBoardWithFile(@RequestBody CreateAndEditBoardRequest request,
//	        @RequestPart("files") List<MultipartFile> files) {
//	    try {
//	        Board board = boardService.insertBoard(request, files); // 게시글과 파일들을 함께 처리
//
//	        return ResponseEntity.ok("파일 업로드 성공");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
//	    }
//	}






//	@PostMapping("/board/boardWrite")
//	@CrossOrigin
//	public ResponseEntity<String> insertBoardWithFile(@RequestPart("boardData") CreateAndEditBoardRequest boardData) {
//	    try {
//	        List<MultipartFile> files = boardData.getFiles();
//
//	        Board board = boardService.insertBoard(boardData, files); // 게시글과 파일들을 함께 처리
//
//	        return ResponseEntity.ok("파일 업로드 성공");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
//	    }
//	}


	
	
	
	//	@PostMapping("/board/boardWrite")
//	@CrossOrigin
//	public ResponseEntity<String> inserBoardWithFile(
//			@RequestParam("request") CreateAndEditBoardRequest request,
//			@RequestParam("files") List<MultipartFile> files
//	) {
//	    try {
//	        Board board = boardService.insertBoard(request, files); // 게시글과 파일들을 함께 처리
//
//	        return ResponseEntity.ok("Data Input Completed with file upload");
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
//	    }
//	}
	

	
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
