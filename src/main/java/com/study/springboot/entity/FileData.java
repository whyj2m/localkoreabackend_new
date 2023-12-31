package com.study.springboot.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Board_attach")
public class FileData {

//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "fileData_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String uuid; // uuid
    private String origin; // 원본파일
    private String filePath; // 저장파일
    private Long boardBno; // 게시글번호

    @Builder
    public FileData(String uuid, String origin, String filePath, Long boardBno) {
        super();
        this.uuid = uuid;
        this.origin = origin;
        this.filePath = filePath;
        this.boardBno = boardBno;
    }
    
 // FileData에서 filePath를 이용하여 파일을 읽어오는 메서드
    public byte[] readFileData(FileData fileData) throws IOException {
        String filePath = fileData.getFilePath();
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}



