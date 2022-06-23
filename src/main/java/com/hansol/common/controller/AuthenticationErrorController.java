package com.hansol.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hansol.common.payload.CommonResult;
import com.hansol.common.payload.ResponseType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "[ERROR] Authentication Error", description = "인증/인가 오류 경로")
public class AuthenticationErrorController {
	
	@Operation(summary = "401, 로그인 필요")
	@GetMapping(value = "/error/unauthenticated")
	public ResponseEntity<?> unauthenticated() {
		return CommonResult.ofFailure(ResponseType.UNAUTHENTICATED);
	}
	
	@Operation(summary = "401, 토큰 오류")
	@GetMapping(value = "/error/invalid-token")
	public ResponseEntity<?> invalidToken() {
		return CommonResult.ofFailure(ResponseType.INVALID_TOKEN);
	}
	
	@Operation(summary = "403, 접근 권한 없음")
	@GetMapping(value = "/error/unauthorized")
	public ResponseEntity<?> unauthorized() {
		return CommonResult.ofFailure(ResponseType.UNAUTHORIZED);
	}
}