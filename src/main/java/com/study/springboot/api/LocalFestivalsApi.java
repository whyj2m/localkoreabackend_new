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

import com.study.springboot.api.request.CreateAndEditLocalFestivalRequest;
import com.study.springboot.api.response.LocalFestivalsDetail;
import com.study.springboot.api.response.LocalFestivalsList;
import com.study.springboot.entity.Location;
import com.study.springboot.service.LocalFestivalsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocalFestivalsApi {
	
	@Autowired
	private LocalFestivalsService localFestivalsService;
	
	@GetMapping("/localFestivals")
	public List<LocalFestivalsList> getLocalFestivalsList(){
		return localFestivalsService.findAllFestivals();
	}
	
	@GetMapping("/localFestival/{festivalNo}")
	public LocalFestivalsDetail getFestival(
			@PathVariable(name="festivalNo") Long festivalNo
			) {
		return localFestivalsService.findById(festivalNo);
	}

	@GetMapping("/localFestivals/{localNo}")
	public List<LocalFestivalsList> getFestivalByLocalNo(
			@PathVariable(name="localNo") Location localNo
			) {
		return localFestivalsService.findByLocalNo(localNo);
	}
	
	@PostMapping("/localFestival")
	public ResponseEntity<String> insertLocalFestival(
			@RequestBody CreateAndEditLocalFestivalRequest request
			){
		try {
			localFestivalsService.insertLocalFestivals(request);
			return ResponseEntity.ok("Data Input Completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error" + e.getMessage());
		}
	}
	
	@PutMapping("/localFestival/{festivalNo}")
	public void editLocalFestivals(
			@PathVariable(name="festivalNo") Long festivalNo,
			@RequestBody CreateAndEditLocalFestivalRequest request
			) {
		localFestivalsService.editLocalFestivals(festivalNo, request);
	}
	
	@DeleteMapping("/localFestival/{festivalNo}")
	public void deleteFestival(
			@PathVariable(name="festivalNo") Long festivalNo
			) {
		localFestivalsService.deleteLocalFestivals(festivalNo);
	}

}
