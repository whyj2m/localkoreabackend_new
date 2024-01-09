package com.study.springboot.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.api.request.CreateAndEditBoardRequest;
import com.study.springboot.api.request.CreateAndEditHeartRequest;
import com.study.springboot.api.request.CreateAndEditLocationRequest;
import com.study.springboot.api.response.BoardDetail;
import com.study.springboot.api.response.BoardList;
import com.study.springboot.api.response.HeartDetail;
import com.study.springboot.api.response.HeartList;
import com.study.springboot.api.response.LocationDetail;
import com.study.springboot.api.response.LocationList;
import com.study.springboot.service.BoardService;
import com.study.springboot.service.HeartService;
import com.study.springboot.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HeartApi {
	
	@Autowired
	private HeartService heartService;
	
	@GetMapping("/hearts")
	public List<HeartList> getHeartList(){
		return heartService.findAllHearts();
	}
	
	@GetMapping("/hearts/{memberid}")
	public List<HeartDetail> getHeartsByUserId(@PathVariable(name = "memberid") String memberId) {
	    return heartService.findHeartsByUserId(memberId);
	}
	
	@GetMapping("/heart/{heartNo}")
	public HeartDetail getHeart(
			@PathVariable(name = "heartNo") Long heartNo
			) {
		return heartService.findById(heartNo);
	}
	
	@GetMapping("/hearts/details/{memberid}")
    public List<HeartDetail> getHeartDetailsByUserId(@PathVariable(name = "memberid") String memberId) {
        return heartService.findHeartDetailsByUserId(memberId);
    }
	
	@PostMapping("/heart")
	public ResponseEntity<String> inserHeart(
			@RequestBody CreateAndEditHeartRequest request
			){
		try {
			heartService.insertHeart(request);
			return ResponseEntity.ok("Data Input Completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
		}
	}
	
//	@PutMapping("/heart/{heartNo}")
//	public void editHeart(
//			@PathVariable(name = "heartNo") Long heartNo,
//			@RequestBody CreateAndEditHeartRequest request
//			) {
//		heartService.editHeart(heartNo, request);
//	}
	
	@DeleteMapping("/heart/{heartNo}")
	public void deleteBoard(
			@PathVariable(name = "heartNo") Long heartNo
			) {
		heartService.deleteHeart(heartNo);
	}
}
