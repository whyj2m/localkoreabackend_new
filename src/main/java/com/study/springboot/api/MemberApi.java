package com.study.springboot.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.api.request.CreateMemberRequest;
import com.study.springboot.api.request.EditMemberInfo;
import com.study.springboot.api.request.EditMemberPassword;
import com.study.springboot.api.request.LoginRequest;
import com.study.springboot.api.response.MemberDetail;
import com.study.springboot.api.response.MemberList;
import com.study.springboot.service.MailService;
import com.study.springboot.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class MemberApi {
	
	private final MemberService memberService;
	private final MailService mailService;
	
	@GetMapping("/members")
	public List<MemberList> getMemberList(){
		return memberService.findAllMembers();
	}
	
	@GetMapping("/members/{id}")
	public MemberDetail getMemberDetail(
			@PathVariable(name="id") String id
			) {
		return memberService.findById(id);
	}
	@GetMapping("/mypage/{id}")
	public MemberDetail getMember(
			@PathVariable(name="id") String id
			) {
		return memberService.findById(id);
	}
	
	// ID 중복 확인
	@CrossOrigin
	@PostMapping("/signup/checkId")
	public ResponseEntity<Boolean> checkIdDuplicate(@RequestBody Map<String, String> idMap) {
	    String id = idMap.get("id");

	    try {
	        boolean isDuplicate = memberService.checkIdDuplicate(id);
	        return ResponseEntity.ok(isDuplicate);
	    } catch (Exception e) {
	        // 예외 발생 시 로그 출력
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
	    }
	}
	
	@CrossOrigin
	@ResponseBody
	@PostMapping("/signup/sendMail")
	public ResponseEntity<String> sendMail(@RequestBody Map<String, String> emailMap) {
	    String email = emailMap.get("email");
	    try {
	        int code = mailService.sendMail(email);
	        String vc = String.valueOf(code);
	        return ResponseEntity.ok(vc);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
	    }
	}
	
	@CrossOrigin
	@PostMapping("/signup/verifyCode")
	public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> verificationCodeMap) {
        String enteredCode = verificationCodeMap.get("verificationCode");
        int savedCode = mailService.getSavedCode(); // 메일로 발송된 코드
        String mailCode = String.valueOf(savedCode);
        if (enteredCode.equals(mailCode)) {
        	log.info("Entered Code: {}", enteredCode);
        	log.info("Saved Code: {}", mailCode);
            // 인증 성공
            return ResponseEntity.ok("인증 성공");
        } else {
            // 인증 실패
            return ResponseEntity.badRequest().body("인증 실패");
        }
    }

	@CrossOrigin
	@PostMapping("/signup")
	public ResponseEntity<String> insertMember(@RequestBody CreateMemberRequest request) {
	    String id = request.getId();
	    try {
	        if (memberService.checkIdDuplicate(id)) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 id 입니다.");
	        }

	        memberService.insertMember(request);
	        return ResponseEntity.ok("회원가입 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
	    }
	}
	
	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		return memberService.login(request);
	}
	
	@PutMapping("/mypage/{id}/editInfo")
	public ResponseEntity<String> editInfo(
			@PathVariable(name="id") String id,
			@RequestBody EditMemberInfo request
			) {
		memberService.editMemberInfo(id, request);
		return ResponseEntity.ok("회원정보가 성공적으로 수정되었습니다.");
	}
	
	@PutMapping("/mypage/{id}/editPw")
	public ResponseEntity<String> editPw(
			@PathVariable(name="id") String id,
			@RequestBody EditMemberPassword request
			) {
		memberService.editMemberPw(id, request);
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	}
	
	@DeleteMapping("/member/{id}")
	public void deleteMember(
			@PathVariable(name="id") String id
			) {
		memberService.deleteMember(id);
	}

}
