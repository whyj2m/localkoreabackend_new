package com.study.springboot.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.springboot.api.request.CreateAndEditMemberRequest;
import com.study.springboot.api.request.EditMemberInfo;
import com.study.springboot.api.response.MemberDetail;
import com.study.springboot.api.response.MemberList;
import com.study.springboot.entity.Member;
import com.study.springboot.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	public Member insertMember(CreateAndEditMemberRequest request) {
		Member member = Member.builder()
							.id(request.getId())
							.email(request.getEmail())
							.password(request.getPassword())
							.name(request.getName())
							.phoneNum(request.getPhoneNum())
							.signUpAt(ZonedDateTime.now())
							.updatedAt(ZonedDateTime.now())
							.build();
		memberRepository.save(member);
		
		return member;
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
	
	public void editMember(String id, CreateAndEditMemberRequest request) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("존재하지 않는 id입니다."));
		member.changeMemberDetail(request.getPassword(), request.getName(), request.getPhoneNum());
		memberRepository.save(member);
		
	}
	
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

}
