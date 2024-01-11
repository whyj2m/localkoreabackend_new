package com.study.springboot.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.BoardReply;
import com.study.springboot.entity.FileData;
import com.study.springboot.entity.category.BoardCategory;
import com.study.springboot.entity.category.LocationCategory;
import com.study.springboot.repository.BoardCategoryRepository;
import com.study.springboot.repository.BoardReplyRepository;
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
	private final BoardReplyRepository boardReplyRepository;
	
	 // 파일업로드
    private final String FOLDER_PATH = "c:\\images\\";
	private final FileDataRepository fileDataRepository;
	
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
	
	// bno별 관광지 추천 상세 조회
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
                .locationCno(board.getLocno().getLocno())
                .location(board.getLocation())
	            .build();
	}

	// bno별 이미지조회
//	public byte[] downloadImgeSystem(Long boardBno) throws IOException {
//	    List<FileData> fileDataList = fileDataRepository.findByBoardBno(boardBno);
//	    
//	    if (fileDataList.isEmpty()) {
//	        throw new RuntimeException("FileData not found for board bno: " + boardBno);
//	    }
//	    
//	    FileData fileData = fileDataList.get(0); // Or determine the appropriate fileData in the list
//	    
//	    String filePath = fileData.getFilePath();
//	    
//	    return Files.readAllBytes(new File(filePath).toPath());
//	}
	
	// bno별 이미지조회

	public byte[] downloadImageSystem(Long boardBno) {
	    List<FileData> fileDataList = fileDataRepository.findByBoardBno(boardBno);

	    if (fileDataList.isEmpty()) {
	        return null; // 이미지가 없으면 null 반환
	    }

	    FileData fileData = fileDataList.get(0); // Or determine the appropriate fileData in the list

	    String filePath = fileData.getFilePath();

	    try {
	        return Files.readAllBytes(Paths.get(filePath));
	    } catch (IOException e) {
	        e.printStackTrace(); // 에러 처리 필요
	        return null;
	    }
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
	
	
	// 여형메이트 bno별 상세조회 
	@Transactional
	public BoardDetail getCompanyDetail(Long bno) {
	    Board board = boardRepository.findByBno(bno).orElseThrow(() -> new EntityNotFoundException("없는 글번호 입니다.: " + bno));
	    
	    return BoardDetail.builder()
	            .bno(board.getBno())
	            .title(board.getTitle())
//	            .content(board.getContent())
	            .viewCnt(board.getViewCnt())
	            .regDate(board.getRegDate())
//	            .updateDate(board.getUpdateDate())
	            .id(board.getId())
                .locationCno(board.getLocno().getLocno())
                .location(board.getLocation())
	            .build();
	}
	
	
	
	
	// 글 작성
	public Board insertBoard(CreateAndEditBoardRequest request, 
	        @RequestParam(required = false) List<MultipartFile> files) throws IOException {
	    log.info("insert");
	    BoardCategory boardCategory = boardCategoryRepository.findById(request.getBoardCno()).orElse(null);
	    LocationCategory locationCategory = locationCategoryRepository.findById(request.getLocationCno()).orElse(null);
	    
	    // 파일 첨부 확인
	    boolean filesAttached = files != null && !files.isEmpty();
	    
	    // 첨부 파일 없을 시 빈 리스트로 설정
	    if (!filesAttached) {
	        files = new ArrayList<>(); // 파일 리스트를 빈 리스트로 설정
	    }
	    
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
	    
	    Long bno = board.getBno();
	    
	    // 파일 첨부가 있는 경우에만 파일 업로드 수행
	    if (filesAttached) {
	        String folderPath = FOLDER_PATH + bno + File.separator;
	        File folder = new File(folderPath);

	        if (!folder.exists()) {
	            folder.mkdirs(); // 폴더 생성
	        }

	        // 파일 업로드
	        for (MultipartFile file : files) {
	            String filePath = folderPath + file.getOriginalFilename();
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

	    // 게시글에 첨부된 파일을 가져와 삭제
	    List<FileData> attachments = fileDataRepository.findByBoardBno(board.getBno());

	    if (attachments != null && !attachments.isEmpty()) {
	        for (FileData attachment : attachments) {
	            // 첨부 파일 삭제
	            deleteAttachment(attachment);
	        }
	    }
	    boardRepository.delete(board);
	}

	// 첨부 파일 삭제 메서드
	private void deleteAttachment(FileData attachment) {
	    // 파일 시스템에서 파일 삭제
	    File file = new File(attachment.getFilePath());
	    if (file.exists()) {
	        file.delete();
	    }
	    // DB에서 첨부 파일 삭제
	    fileDataRepository.delete(attachment);
	}
		
	// 조회수
	public void viewCount(Long bno) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다."));
		board.viewCount();
		boardRepository.save(board);
	}

	// 댓글 작성
	@Transactional
	public void BoardReply(Long bno, String content) {
	    Board board = boardRepository.findByBno(bno)
	            .orElseThrow(() -> new EntityNotFoundException("해당 bno의 게시글을 찾을 수 없습니다: " + bno));

	    BoardReply reply = BoardReply.builder()
	            .content(content)
	            .regDate(ZonedDateTime.now())
	            .build();

	    reply.setBoard(board); // 댓글에 해당하는 게시글 설정

	    boardReplyRepository.save(reply);
	}

	// 댓글조회
//	@Transactional
//	public List<Object[]> findReply(Long bno) {
//	    List<BoardReply> replies = boardReplyRepository.findByBoard_Bno(bno);
//
//	    if (replies.isEmpty()) {
//	        throw new EntityNotFoundException("해당 번호의 댓글이 없습니다.: " + bno);
//	    }
//
//	    return replies.stream()
//	            .map(reply -> new Object[]{ reply.getRno(), reply.getId(), reply.getContent(), reply.getRegDate()})
//	            .collect(Collectors.toList());
//	}

	
//	@Transactional
//	public List<BoardList> findByReply(Long bno) {
//	    List<BoardReply> replys = boardReplyRepository.findByBno(bno);
//
//	    if (replys.isEmpty()) {
//	        throw new EntityNotFoundException("해당 번호의 댓글이 없습니다.: " + bno);
//	    }
//
//	    return replys.stream()
//	            .map(reply -> {
//	                // 댓글 내용을 가져와서 BoardList 객체로 매핑
//	                return BoardList.builder()
//	                        .bno(reply.getBoard().getBno()) // 댓글의 boardBno를 사용하여 데이터 추출
//	                        .content(reply.getContent())
//	                        .regDate(reply.getRegDate())
//	                        .id(reply.getId())
//	                        .build();
//	            })
//	            .collect(Collectors.toList());
//	}



	
	
	
	
//	public BoardReply findByReply(Long boardBno) {
//	    return boardReplyRepository.findByBno(boardBno)
//	            .orElseThrow(() -> new EntityNotFoundException("없는 글번호 입니다.: " + boardBno));
//	}
	
	
	
	
	
	
	
//	// 댓글 조회
//	public BoardReply findByReply(Long bno) {	    
//		 List<BoardReply> replies = boardReplyRepository.findByBno(bno);
//		    return !replies.isEmpty() ? replies.get(0) : null;
//	}

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