package com.study.springboot.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.springboot.api.request.CreateMemberRequest;
import com.study.springboot.api.request.EditMemberInfo;
import com.study.springboot.api.request.EditMemberPassword;
import com.study.springboot.api.request.LoginRequest;
import com.study.springboot.api.response.LoginResponse;
import com.study.springboot.api.response.MemberDetail;
import com.study.springboot.api.response.MemberList;
import com.study.springboot.config.jwt.TokenProvider;
import com.study.springboot.entity.Member;
import com.study.springboot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final TokenProvider tokenProvider;
	
	public Member insertMember(CreateMemberRequest request) {
		Member member = Member.builder()
							.id(request.getId())
							.email(request.getEmail())
							.password(bCryptPasswordEncoder.encode(request.getPassword()))
							.name(request.getName())
							.phoneNum(request.getPhoneNum())
							.signUpAt(ZonedDateTime.now())
							.updatedAt(ZonedDateTime.now())
							.role(request.getRole())
							.authProvider(request.getAuthProvider())
							.build();
		memberRepository.save(member);
		
		return member;
	}
	
	public boolean checkIdDuplicate(String id) {
		return memberRepository.existsById(id);
	}
	
	public ResponseEntity<LoginResponse> login(LoginRequest request) {
		Member member = memberRepository.findById(request.getId()).orElseThrow(()->new IllegalArgumentException(request.getId()));
		
		// 사용자 존재, 비밀번호 일치 >> 로그인 성공
		if(member != null && bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())) {
			// 토큰 만료시간 설정 후 생성
			Duration expirationTime = Duration.ofMinutes(30);
			String token = tokenProvider.generateToken(member, expirationTime);
			
			// 로그인 성공 및 JWT 토큰반환
			LoginResponse response = new LoginResponse("Login successful!", token, member.getId());
			return ResponseEntity.ok(response);
		} else { // 로그인 실패
			LoginResponse response = new LoginResponse("Login failed.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}
	
	public List<MemberList> findAllMembers(){
		List<Member> members = memberRepository.findAll();
		return members.stream().map((member) -> MemberList.builder()
								.id(member.getId())
								.email(member.getEmail())
								.password(member.getPassword())
								.name(member.getName())
								.phoneNum(member.getPhoneNum())
								.signUpAt(member.getSignUpAt())
								.updatedAt(member.getUpdatedAt())
								.build()
								).toList();
	}
	
	public MemberDetail findById(String id) {
		Member member = memberRepository.findById(id).orElseThrow();
		
		return MemberDetail.builder()
									.id(member.getId())
									.email(member.getEmail())
									.password(member.getPassword())
									.name(member.getName())
									.phoneNum(member.getPhoneNum())
									.signUpAt(member.getSignUpAt())
									.updatedAt(member.getUpdatedAt())
									.build();
	}
	
//	public void editMember(String id, CreateMemberRequest request) {
//		Member member = memberRepository.findById(id)
//				.orElseThrow(() -> new RuntimeException("존재하지 않는 id입니다."));
//		member.changeMemberDetail(request.getPassword(), request.getName(), request.getPhoneNum());
//		memberRepository.save(member);
//	}
	
	public void deleteMember(String id) {
		Member member = memberRepository.findById(id).orElseThrow();
		memberRepository.delete(member);
	}

	public void editMemberInfo(String id, EditMemberInfo request) {
		Member member = memberRepository.findById(id)
				.orElseThrow(()->new RuntimeException("존재하지 않는 id입니다."));
		member.changeMemberDetail(request.getEmail(), request.getName(), request.getPhoneNum());
		memberRepository.save(member);
	}

	public void editMemberPw(String id, EditMemberPassword request) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("존재하지 않는 id입니다."));
		member.changeMemberPassword(request.getPassword());
		memberRepository.save(member);
	}

}
