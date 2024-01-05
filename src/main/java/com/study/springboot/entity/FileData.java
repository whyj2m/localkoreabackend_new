package com.study.springboot.entity;

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
//@AllArgsConstructor
@Table(name = "Board_attach")
public class FileData {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "fileData_seq")
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
}



//package com.study.springboot.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "Board_FileData")
//public class FileData {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fileData_seq")
//	private Long id;
//	private String name;
//	private String type;
//	private String filePath;
//	
//	@Builder
//	public FileData(String name, String type, String filePath) {
//		super();
//		this.name = name;
//		this.type = type;
//		this.filePath = filePath;
//	}
//}
