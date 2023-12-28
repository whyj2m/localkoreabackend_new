package com.study.springboot.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Member")
@Entity
public class Member {

	@Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private ZonedDateTime signUpAt; // 회원 가입 일시 

	private ZonedDateTime updatedAt; // 비밀 번호 수정 일시
	private boolean admin;

	
	public void changeMemberDetail(String email, String name, String phoneNum) {
		this.email = email;
		this.name = name;
		this.phoneNum = phoneNum;
		this.updatedAt = ZonedDateTime.now();
	}


//    public void patch(Member member) {
//        if(member.email != null) {
//            this.email = member.email;
//        }
//        if(member.password != null) {
//            this.password = member.password;
//        }
//    }

}
