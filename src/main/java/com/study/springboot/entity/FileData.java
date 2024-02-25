package com.study.springboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.s3.AmazonS3;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Board_attach")
public class FileData {

	 private static AmazonS3 amazonS3;
	  
	 @Transient
	 @Value("${cloud.aws.s3.bucket}")
	 private String bucket;
	 
    @Id
    private String uuid; // 파일 식별자
    private String origin; // 원본 파일명
    private String filePath; // 저장된 파일 경로
    @Column(name = "board_bno")
    private Long boardBno; // 게시글 번호

    @Builder
    public FileData(String uuid, String origin, String filePath, Long boardBno, String s3Key) {
        this.uuid = uuid;
        this.origin = origin;
        this.filePath = filePath;
        this.boardBno = boardBno;
    }
    
    public String getImageUrl() {
        // UUID를 사용하여 S3 URL 생성
        return amazonS3.getUrl(bucket, uuid).toString();
    }
    
    // 파일 데이터 읽기 메서드
//    public byte[] readFileData(ResponseInputStream<GetObjectResponse> responseInputStream) throws IOException {
//        return IOUtils.toByteArray(responseInputStream);
//    }
    
	 // S3에서 파일 데이터 읽기 메서드
//	    public byte[] readS3FileData(AmazonS3 amazonS3, String bucketName) throws IOException {
//	        S3Object s3Object = amazonS3.getObject(bucketName, uuid);
//	        try (InputStream inputStream = s3Object.getObjectContent()) {
//	            return IOUtils.toByteArray(inputStream);
//	        } finally {
//	            s3Object.close();
//	        }
//	    }
	   
}
