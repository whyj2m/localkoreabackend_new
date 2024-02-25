package com.study.springboot.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.BoardReply;
import com.study.springboot.entity.FileData;
import com.study.springboot.entity.Member;
import com.study.springboot.entity.category.BoardCategory;
import com.study.springboot.entity.category.LocationCategory;
import com.study.springboot.repository.BoardCategoryRepository;
import com.study.springboot.repository.BoardReplyRepository;
import com.study.springboot.repository.BoardRepository;
import com.study.springboot.repository.FileDataRepository;
import com.study.springboot.repository.LocationCategoryRepository;
import com.study.springboot.repository.MemberRepository;

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
	private final MemberRepository memberRepository;
	
	// 파일업로드
    private final String FOLDER_PATH = "c:\\images\\";
	private final FileDataRepository fileDataRepository;
	
	private final AmazonS3Client amazonS3Client;
	 private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
	
    // 글 작성
    public Board insertBoard(CreateAndEditBoardRequest request, 
            @RequestParam(value = "id") String id,
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

        Member member = memberRepository.findById(id).orElse(null); // 멤버에서 id

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .regDate(ZonedDateTime.now())
                .updateDate(ZonedDateTime.now())
                .cno(boardCategory)
                .locno(locationCategory)
                .location(request.getLocation())
                .id(member)
                .build();
        boardRepository.save(board);

        // 파일 첨부가 있는 경우에만 파일 업로드 수행
        if (filesAttached) {
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString();
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String cdn = uuid + ext;

                // Amazon S3에 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                amazonS3Client.putObject(new PutObjectRequest(bucket, cdn, file.getInputStream(), metadata));

             // DB에 파일 정보 저장
                FileData fileData = FileData.builder()
                    .uuid(file.getOriginalFilename()) // S3에 저장된 파일명을 원본 파일명으로 사용
                    .origin(file.getOriginalFilename())
                    .boardBno(board.getBno())
                    .build();

                fileDataRepository.save(fileData);
            }
        }

        return board;
    }

	
	// 관광지 추천 게시글 조회
	@Transactional
	public List<BoardList> findByTouristSpot() {

		BoardCategory category = BoardCategory.builder().cno(1L).build();
	    
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
	    Board board = boardRepository.findByBno(bno).orElseThrow(() -> new EntityNotFoundException("없는 게시글입니다 : " + bno));
	    
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
		
	// 이미지 조회
	public byte[] getImageData(Long boardBno) throws IOException {
        String imageUrl = getImageUrl(boardBno);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                return IOUtils.toByteArray(in);
            }
        }
        return null;
    }
	
	// 이미지 주소
	public String getImageUrl(Long boardBno) {
	    String imageKey = "images/" + boardBno + ".jpg";
	
        // S3 URL 생성
        return amazonS3Client.getUrl(bucket, imageKey).toString();
	}
		
	// 이미지 정보
	public List<FileData> findByBoardBno(Long boardBno) {
		return fileDataRepository.findByBoardBno(boardBno);
	}
	
	// local 이미지 조회
//		@GetMapping("/api/images/{bno}")
//		@CrossOrigin
//		public ResponseEntity<?> getImage(@PathVariable("bno") Long bno) throws IOException{
//			byte[] downloadImage = boardService.downloadImageSystem(bno);
//			if(downloadImage != null) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.contentType(MediaType.valueOf("image/png"))
//						.body(downloadImage);
//			}else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//		}

	// 여행메이트 게시글 조회
	public List<BoardList> findByCompany() {
	    BoardCategory category = BoardCategory.builder().cno(2L).build();
		    
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
	
	// 여형메이트 bno별 상세 조회
	@Transactional
	public BoardDetail getCompanyDetail(Long bno) {
	    Board board = boardRepository.findByBno(bno).orElseThrow(() -> new EntityNotFoundException("없는 글번호입니다"));
	    
	    return BoardDetail.builder()
	            .bno(board.getBno())
	            .title(board.getTitle())
	            .viewCnt(board.getViewCnt())
	            .regDate(board.getRegDate())
	            .id(board.getId())
                .locationCno(board.getLocno().getLocno())
                .location(board.getLocation())
	            .build();
	}
	
	// 글 수정
	public void editBoard(Long bno, CreateAndEditBoardRequest request) {
		Board board = boardRepository.findById(bno)
				.orElseThrow(() -> new RuntimeException("없는 글번호입니다"));
		
	    BoardCategory boardCategory = boardCategoryRepository.findById(request.getBoardCno())
	            .orElseThrow(() -> new RuntimeException("없는 카테고리입니다"));

	    LocationCategory locationCategory = locationCategoryRepository.findById(request.getLocationCno())
	            .orElseThrow(() -> new RuntimeException("없는 지역입니다"));
	    
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

	    // 댓글 삭제
	    List<BoardReply> replies = boardReplyRepository.findByBoardBno(board.getBno());
	    if (replies != null && !replies.isEmpty()) {
	        for (BoardReply reply : replies) {
	            deleteReply(reply.getRno());
	        }
	    }

	    boardRepository.delete(board);
	}
	
	// 댓글 단일 삭제
	public void deleteReply(Long rno) {
		    boardReplyRepository.deleteById(rno);
	}	
	
	// 첨부 파일 삭제
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
				.orElseThrow(()-> new RuntimeException("없는 게시글입니다 : " + bno));
		board.viewCount();
		boardRepository.save(board);
	}
	
	// 댓글 작성
	@Transactional
	public void BoardReply(Long bno, String content, Member member) {
	    Board board = boardRepository.findByBno(bno)
	            .orElseThrow(() -> new EntityNotFoundException("없는 게시글입니다 : " + bno));

	    BoardReply reply = BoardReply.builder()
	            .content(content)
	            .regDate(ZonedDateTime.now())
	            .id(member)
	            .build();

	    reply.setBoard(board);

	    boardReplyRepository.save(reply);
	}

	// 댓글 조회
	@Transactional
	public List<Map<String, Object>> findReply(Long bno) {
	    List<BoardReply> replies = boardReplyRepository.findByBoardBno(bno);

	    if (replies.isEmpty()) {
	    }

	    return replies.stream()
	            .map(reply -> {
	                Map<String, Object> replyMap = new HashMap<>();
	                replyMap.put("rno", reply.getRno());
	                replyMap.put("id", reply.getId() != null ? reply.getId().getId() : null);
	                replyMap.put("email", reply.getId().getEmail());
	                replyMap.put("name", reply.getId().getName());
	                replyMap.put("content", reply.getContent());
	                replyMap.put("regDate", reply.getRegDate());
	                return replyMap;
	            })
	            .collect(Collectors.toList());
	}

	

	/**
     * @author bhy98 백혜윤
     * 작성자 id로 게시글 조회 - 관광지 추천
     */
	@Transactional
	public List<BoardList> tourSpotFindById(String userId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다: " + userId));
		BoardCategory category = boardCategoryRepository.findByCname("관광지 추천");
		List<Board> boards = boardRepository.findByCnoAndId(category, member);

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
						.build()
						).collect(Collectors.toList());
	}

	/**
     * @author bhy98 백혜윤
     * 작성자 id로 게시글 조회 - 여행 메이트
     */
	@Transactional
	public List<BoardList> travelmateFindById(String userId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다: " + userId));
		BoardCategory category = boardCategoryRepository.findByCname("여행 메이트");
		List<Board> boards = boardRepository.findByCnoAndId(category, member);

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
						.build()
						).collect(Collectors.toList());
	}
	
	/**
	* @author bhy98 백혜윤
	 * 작성자 id로 댓글 조회
	*/
	@Transactional
	public List<Map<String, Object>> replyFindById(String userId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다: " + userId));
		List<BoardReply> replyList = boardReplyRepository.findById(member);
		
		return replyList.stream()
				.map(reply -> {
					Map<String, Object> replyMap = new HashMap<>();
					replyMap.put("rno", reply.getRno());
					replyMap.put("content", reply.getContent());
					replyMap.put("regDate", reply.getRegDate());
					replyMap.put("id", reply.getId());
					replyMap.put("bno", reply.getBoard().getBno());
					replyMap.put("title", reply.getBoard().getTitle());
					replyMap.put("location", reply.getBoard().getLocation());
					return replyMap;
				}).collect(Collectors.toList());
	}

	public FileData getFileDataByBoardBno(Long bno) {
		// TODO Auto-generated method stub
		return null;
	}
}