package com.hansol.schedule.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.common.payload.CommonResult;
import com.hansol.common.payload.ResourceResult;
import com.hansol.common.payload.ResponseType;
import com.hansol.schedule.dto.JobRequest;
import com.hansol.schedule.dto.JobStateRequest;
import com.hansol.schedule.dto.JobUpdateRequest;
import com.hansol.schedule.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController 
@RequestMapping("/api/scheduler")
@Tag(name = "[API] Schedule API", description = "스케줄 API")
public class ScheduleContoller {
	
	//@Autowired 보다 안정적인 방법
	private final ScheduleService scheduleService;
	
	@Operation(summary="스케줄 등록")
	@PostMapping(value = "/jobs")
	public ResponseEntity<?> addScheduleJobs(@RequestBody @Valid JobRequest jobRequest){
		scheduleService.addJob(jobRequest);
		return CommonResult.ofSuccess(ResponseType.OK);
		// ResponseEntity: HttpStatus, HttpHeaders, HttpBody를 포함
	}
	
	@Operation(summary="스케줄 목록")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/jobs")
	public ResponseEntity<?> scheduleList(@RequestParam(value="nowPage", required=false)String nowPage){
		
		if(nowPage==null) {
			nowPage="1";
		}
		return ResourceResult.ofSuccess(ResponseType.OK, scheduleService.getscheduleList(nowPage));
	}
	
	
    @Operation(summary = "스케줄 상세 조회")
    @PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/jobs/{labelId}")
	public ResponseEntity<?> getSchedule(@PathVariable int labelId) {
		return ResourceResult.ofSuccess(ResponseType.OK, scheduleService.getSchedule(labelId));
	}
    
    @Operation(summary = "스케줄 수정")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/jobs/{labelId}")
	public ResponseEntity<?> updateSchedule(@PathVariable int labelId, @RequestBody @Valid JobUpdateRequest payload) {
    	scheduleService.updateSchedule(labelId, payload);
    	
		return ResourceResult.ofSuccess(ResponseType.OK, labelId);
	}
    
    @Operation(summary = "스케줄 상태 수정")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/jobs/{labelId}/state")
	public ResponseEntity<?> updateScheduleState(@PathVariable int labelId, @RequestBody @Valid JobStateRequest payload) {
    	scheduleService.updateScheduleState(labelId, payload);
    	
		return ResourceResult.ofSuccess(ResponseType.OK, labelId);
	}
    
    
    @Operation(summary="실행 로그 목록")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value="/logs")
    public  ResponseEntity<?> logList(@RequestParam(value="nowPage", required=false)String nowPage){
    	if(nowPage==null) {
			nowPage="1";
		}
    	return ResourceResult.ofSuccess(ResponseType.OK, scheduleService.getLogList(nowPage));
    }
    
    @Operation(summary="스케줄 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/jobs/{labelId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int labelId){
    	scheduleService.deleteSchedule(labelId);
    	return ResourceResult.ofSuccess(ResponseType.OK);
    }
}