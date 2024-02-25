package com.study.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileUploadService {

    private final S3Client amazonS3Client;
    private final String bucketName;
    
    @Value("${cloud.aws.s3.bucket}")
    private String injectedBucketName;
    
    public S3FileUploadService() {
        this.amazonS3Client = null; 
        this.bucketName = null;
    }

    public S3FileUploadService(S3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = injectedBucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
    	   String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    	    String s3Key = "test/" + fileName;

    	    ObjectMetadata metadata = new ObjectMetadata();
    	    metadata.setContentType(file.getContentType());
    	    metadata.setContentLength(file.getSize());

    	    // 파일 데이터를 전달할 RequestBody 생성
    	    RequestBody requestBody = RequestBody.fromBytes(file.getBytes());

    	    // putObject 메서드 수정
    	    amazonS3Client.putObject(PutObjectRequest.builder()
    	            .bucket(bucketName)
    	            .key(s3Key)
    	            .metadata(Collections.singletonMap("Content-Type", file.getContentType())) // 메타데이터 설정
    	            .build(), requestBody);

    	    return s3Key;
    }
}
