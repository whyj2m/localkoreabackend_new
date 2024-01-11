package com.study.springboot.api;

import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.request.CreateReplyRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.BoardReply;
import com.study.springboot.entity.FileData;
import com.study.springboot.repository.FileDataRepository;
import com.study.springboot.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApi {
	
	@Autowired
	private BoardService boardService;
	private FileDataRepository fileDataRepository;
	
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
	
	// 관광지 추천 bno별 상세 조회 test
	@GetMapping("/board/company/{bno}")
	@CrossOrigin
	public BoardDetail getCompanyDetail(@PathVariable Long bno) {
		
		BoardDetail board = boardService.findByBno(bno);
		
		boardService.viewCount(bno); // 조회수 1씩 증가
		
		if(board.getViewCnt() == null) {  // viewCnt가 null이면 0으로 설정
			board.setViewCnt(0L);
		}
		
		return boardService.findByBno(bno);
	}
	
//	@GetMapping("/api/images/{bno}")
//	@CrossOrigin
//	public ResponseEntity<?> downImage(@PathVariable("bno") Long bno) throws IOException{
//		byte[] downloadImage = boardService.downloadImageSystem(bno);
//		if(downloadImage != null) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.contentType(MediaType.valueOf("image/png"))
//					.body(downloadImage);
//		}else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		}
//	}

	// 이미지 조회
	@GetMapping("/api/images/{bno}")
	@CrossOrigin
	public ResponseEntity<?> downImage(@PathVariable("bno") Long bno) {
	    byte[] downloadImage = boardService.downloadImageSystem(bno);
	    if (downloadImage != null) {
	        return ResponseEntity.status(HttpStatus.OK)
	                .contentType(MediaType.IMAGE_PNG)
	                .body(downloadImage);
	    } else {
	        // 이미지가 없을 경우 200 상태코드와 기본 이미지를 반환하도록 처리
	        try {
	            byte[] defaultImage = Files.readAllBytes(Paths.get("src/main/resources/static/images/noImg.png"));
	            return ResponseEntity.status(HttpStatus.OK)
	                    .contentType(MediaType.IMAGE_PNG)
	                    .body(defaultImage);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        }
	    }
	}

	// 이미지파일 없어도 500오류 x
	public byte[] downloadImageSystem(Long boardBno) throws IOException {
	    List<FileData> fileDataList = fileDataRepository.findByBoardBno(boardBno);

	    if (fileDataList.isEmpty()) {
	        return null;
	    }

	    FileData fileData = fileDataList.get(0);

	    String filePath = fileData.getFilePath();

	    return Files.readAllBytes(new File(filePath).toPath());
	}

	
	
	// 여행메이트 게시글 조회
	@GetMapping("/board/company")
	@CrossOrigin
	public List<BoardList> getCompanyList(){
		return boardService.findByCompany();
	}

	// 게시글작성
	@PostMapping("/board/boardWrite")
	@CrossOrigin
	public ResponseEntity<String> insertBoardWithFile(
	        @RequestParam(value = "title") String title,
	        @RequestParam(value = "content") String content,
	        @RequestParam(value = "boardCno") Long boardCno,
	        @RequestParam(value = "locationCno") Long locationCno,
	        @RequestParam(value = "location") String location,
	        @RequestParam(required = false) List<MultipartFile> files // 수정된 부분
	) {
	    try {
	        CreateAndEditBoardRequest request = new CreateAndEditBoardRequest();
	        request.setTitle(title);
	        request.setContent(content);
	        request.setBoardCno(boardCno);
	        request.setLocationCno(locationCno);
	        request.setLocation(location);

	        Board board = boardService.insertBoard(request, files); // 게시글과 파일들을 함께 처리

	        return ResponseEntity.ok("백엔드) 파일 업로드 성공");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("백엔드) 게시글 작성 오류" + e.getMessage());
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
	
	// 게시글 삭제
	@DeleteMapping("/board/delete/{bno}")
	@CrossOrigin
	public void deleteBoard(
			@PathVariable(name = "bno") Long bno
			) {
		boardService.deleteBoard(bno);
	}	
	
	// 댓글작성
	@PostMapping("/board/reply")
	@CrossOrigin
	public ResponseEntity<String> insertReply(@RequestBody CreateReplyRequest request){
		try {
			boardService.BoardReply(request.getBno(), request.getContent());
			return ResponseEntity.status(HttpStatus.CREATED).body("댓글 작성 성공");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 실패");
		}	
	}
	
	
	// 댓글 조회
//	@GetMapping("/board/company/{bno}")
//	@CrossOrigin
//    public List<Object[]> getReplyRnoListByBno(@PathVariable Long bno) {
//        return boardService.findReply(bno);
//    }

	


	
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