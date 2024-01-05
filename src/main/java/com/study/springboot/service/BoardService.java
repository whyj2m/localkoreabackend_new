package com.study.springboot.service;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.FileData;
import com.study.springboot.entity.category.BoardCategory;
import com.study.springboot.entity.category.LocationCategory;
import com.study.springboot.repository.BoardCategoryRepository;
import com.study.springboot.repository.BoardRepository;
import com.study.springboot.repository.FileDataRepository;
import com.study.springboot.repository.LocationCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final BoardCategoryRepository boardCategoryRepository;
	private final LocationCategoryRepository locationCategoryRepository;
	 // 파일업로드
    private final String FOLDER_PATH = "c:\\images\\";
	private final FileDataRepository fileDataRepository;
	
	// 게시글 전체 조회
//	public List<BoardList> findAllBoard(){
//		List<Board> boards = boardRepository.findAll();
//		return boards.stream().map((board) -> BoardList.builder()
//							.title(board.getTitle())
//							.content(board.getContent())
//							.viewCnt(board.getViewCnt())
//							.regDate(ZonedDateTime.now())
//							.updateDate(ZonedDateTime.now())
////							.boardCno(board.getCno())
////							.locationCno(board.getLocno())
//							.build()
//							).toList();
//	}
	
	// 관광지 추천 게시글 조회
	@Transactional
	public List<BoardList> findByTourisSpot() {
	    // BoardCategory 객체 생성 및 초기화
	    BoardCategory category = BoardCategory.builder().cno(1L).build();
	    
	    // 해당 카테고리로 게시글 조회
	    List<Board> boards = boardRepository.findByCno(category);
	    
	    // 조회된 게시글을 BoardList로 변환하여 반환
	    return boards.stream()
	            .map(board -> BoardList.builder()
	            		.bno(board.getBno())
	                    .title(board.getTitle())
	                    .content(board.getContent())
	                    .viewCnt(board.getViewCnt())
	                    .regDate(board.getRegDate())
	                    .updateDate(board.getUpdateDate())	    
	                    .id(board.getId())
	                    .boardCno(board.getCno().getCno())
	                    .locationCno(board.getLocno().getLocno())
	                    .location(board.getLocation())
	                    .build())
	            .collect(Collectors.toList());
	}
	
	// bno별 상세 조회
	@Transactional
	public BoardDetail findByBno(Long bno) {
	    Board board = boardRepository.findByBno(bno).orElseThrow(() -> new EntityNotFoundException("없는 글번호 입니다.: " + bno));
	    
	    return BoardDetail.builder()
	            .bno(board.getBno())
	            .title(board.getTitle())
	            .content(board.getContent())
	            .viewCnt(board.getViewCnt())
	            .regDate(board.getRegDate())
	            .updateDate(board.getUpdateDate())
	            .id(board.getId())
	            .build();
	}

	
	// 여행메이트 게시글 조회
	public List<BoardList> findByCompany() {
	    // BoardCategory 객체 생성 및 초기화
	    BoardCategory category = BoardCategory.builder().cno(2L).build();
		    
    	// 해당 카테고리로 게시글 조회
	    List<Board> boards = boardRepository.findByCno(category);
	    	   
	    // 조회된 게시글을 BoardList로 변환하여 반환
	    return boards.stream()
	            .map(board -> BoardList.builder()
	            		.bno(board.getBno())
	                    .title(board.getTitle())
	                    .content(board.getContent())
	                    .viewCnt(board.getViewCnt())
	                    .regDate(board.getRegDate())
	                    .updateDate(board.getUpdateDate())	   
	                    .id(board.getId())
	                    .boardCno(board.getCno().getCno())
	                    .locationCno(board.getLocno().getLocno())
	                    .location(board.getLocation())
	                    .build())
	            .collect(Collectors.toList());
		}	
	
	// 글 작성
	public Board insertBoard(CreateAndEditBoardRequest request, List<MultipartFile> files) throws IOException {
	    log.info("insert");
		BoardCategory boardCategory = boardCategoryRepository.findById(request.getBoardCno()).orElse(null);
	    LocationCategory locationCategory = locationCategoryRepository.findById(request.getLocationCno()).orElse(null);
	    log.info("글작성 전");
	    Board board = Board.builder()
	            .title(request.getTitle())
	            .content(request.getContent())
	            .regDate(ZonedDateTime.now())
	            
	            .updateDate(ZonedDateTime.now())
	            .cno(boardCategory)
	            .locno(locationCategory)
	            .location(request.getLocation())
	            .build();
	    boardRepository.save(board);
	    
	    log.info("글작성은 됩니다");
	    
	    // 파일 업로드
	    for (MultipartFile file : files) {
	        String filePath = FOLDER_PATH + file.getOriginalFilename();
	        FileData fileData = fileDataRepository.save(
	                FileData.builder()
	                        .uuid(file.getOriginalFilename())
	                        .origin(file.getContentType())
	                        .filePath(filePath)
	                        .boardBno(board.getBno())
	                        .build()
	        );
	        file.transferTo(new File(filePath));
	    }

	    return board;
	}

	// 글 수정
	public void editBoard(Long bno, CreateAndEditBoardRequest request) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new RuntimeException("Known bno"));
		
	    BoardCategory boardCategory = boardCategoryRepository.findById(request.getBoardCno())
	            .orElseThrow(() -> new RuntimeException("Known cno"));

	    LocationCategory locationCategory = locationCategoryRepository.findById(request.getLocationCno())
	            .orElseThrow(() -> new RuntimeException("Known lcno"));
	    
		board.changeBoard(request.getTitle(), request.getContent(), boardCategory, locationCategory, request.getLocation());
		boardRepository.save(board);
	}
		
	// 글 삭제
	public void deleteBoard(Long bno) {
		Board board = boardRepository.findById(bno).orElseThrow();
		boardRepository.delete(board);
	}
		
	// 조회수
	public void viewCount(Long bno) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다."));
		board.viewCount();
		boardRepository.save(board);
	}

	// 
//	
//	
//	
//	@Transactional
//	public BoardDetail findById(Long bno) {
//		Board board = boardRepository.findById(bno).orElseThrow();
//		
//		return BoardDetail.builder()
//									.title(board.getTitle())
//									.content(board.getContent())
//									.viewCnt(board.getViewCnt())
//									.regDate(ZonedDateTime.now())
//									.updateDate(ZonedDateTime.now())
//									.build();
//	}
//	
//	@Transactional
//	public void editBoard(Long bno, CreateAndEditBoardRequest request) {
//		Board board = boardRepository.findById(bno)
//				.orElseThrow(() -> new RuntimeException("Known LocalFood"));
//		board.changeBoard(request.getTitle(), request.getContent());
//		boardRepository.save(board);
//		
//	}
//	
//	@Transactional
//	public void deleteBoard(Long bno) {
//		Board board = boardRepository.findById(bno).orElseThrow();
//		boardRepository.delete(board);
//	}

}