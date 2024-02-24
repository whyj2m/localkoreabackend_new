package com.study.springboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Board_attach")
public class FileData {

    @Id
    private String uuid; // 파일 식별자
    private String origin; // 원본 파일명
    private String filePath; // 저장된 파일 경로
    @Column(name = "board_bno")
    private Long boardBno; // 게시글 번호
    private String s3Key; // AWS S3에 저장된 파일의 키

    @Builder
    public FileData(String uuid, String origin, String filePath, Long boardBno, String s3Key) {
        this.uuid = uuid;
        this.origin = origin;
        this.filePath = filePath;
        this.boardBno = boardBno;
        this.s3Key = s3Key;
    }

    // 파일 데이터 읽기 메서드
    public byte[] readFileData(ResponseInputStream<GetObjectResponse> responseInputStream) throws IOException {
        return IOUtils.toByteArray(responseInputStream);
    }
}
