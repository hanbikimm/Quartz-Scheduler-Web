package com.hansol.member.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hansol.common.payload.CommonResult;
import com.hansol.common.payload.ResourceResult;
import com.hansol.common.payload.ResponseType;
import com.hansol.common.utility.SecurityUtils;
import com.hansol.member.domain.Member;
import com.hansol.member.payload.MemberCreationRequest;
import com.hansol.member.payload.MemberLoginRequest;
import com.hansol.member.payload.MemberLoginResponse;
import com.hansol.member.payload.MemberProfileResponse;
import com.hansol.member.payload.TokenResponse;
import com.hansol.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "[API] Member API", description = "회원 API")
public class MemberController {
	private final MemberService memberService;
	
	@Operation(summary = "회원 프로필 조회")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/api/members/profile")
	public ResponseEntity<?> getProfile(@RequestParam("username") String usernameOrEmail) {
		log.info("[INFO] 요청자 정보: {}", SecurityUtils.getUsername());
		
		Member member = memberService.getMember(usernameOrEmail);
		
		return ResourceResult.ofSuccess(ResponseType.OK, MemberProfileResponse.of(member));
	}
	
//    @Operation(summary = "토큰 재발급")
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping(value = "/api/members/token")
//    public ResponseEntity<?> getToken() {
//    	TokenResponse token = TokenResponse.of(memberService.recreateToken());
//    	
//        return ResourceResult.ofSuccess(ResponseType.OK, token);
//    }
	
    @Operation(summary = "아이디 중복 확인")
    @GetMapping(value = "/api/members/username-check")
    public ResponseEntity<?> checkDuplicateUsername(@RequestParam("username") String username) {
        Boolean result = memberService.isDuplicateUsername(username);

        return ResourceResult.ofSuccess(ResponseType.OK, result);
    }

    @Operation(summary = "이메일 중복 확인")
    @GetMapping(value = "/api/members/email-check")
    public ResponseEntity<?> checkDuplicateEmail(@RequestParam("email") String email) {
        Boolean result = memberService.isDuplicateEmail(email);

        return ResourceResult.ofSuccess(ResponseType.OK, result);
    }
	
	@Operation(summary = "회원 가입")
	@PostMapping(value = "/api/members/register")
	public ResponseEntity<?> register(@RequestBody @Valid MemberCreationRequest payload) {
		memberService.register(payload);
		
		return CommonResult.ofSuccess(ResponseType.OK);
	}
	
	@Operation(summary = "회원 로그인")
	@PostMapping(value = "/api/members/login")
	public ResponseEntity<?> login(@RequestBody MemberLoginRequest payload) {
		String token = memberService.login(payload);
		Member member = memberService.getMember(payload.getUsernameOrEmail());
		MemberLoginResponse result = MemberLoginResponse.of(member, token);
		
		return ResourceResult.ofSuccess(ResponseType.OK, result);
	}
}