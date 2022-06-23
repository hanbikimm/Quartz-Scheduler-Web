package com.hansol.member.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
	private int memberId; // PK
	private String username; // 사번 (로그인)
	private String password; // 비밀번호 (로그인)
	private String email; // 사용자 이메일 (로그인)
	private String name; // 사용자 이름
	private RoleType role; // 사용자 권한
//	private String token; // 인증 토큰
	
	private LocalDateTime createdDt; // 생성 날짜
	private LocalDateTime updatedDt; // 수정 날짜
	
	@Builder
	public Member(
			int memberId,
			String username,
			String password,
			String email,
			String name,
			RoleType role,
//			String token,
			LocalDateTime createdDt,
			LocalDateTime updatedDt) {
		this.memberId = memberId;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.role = role;
//		this.token = token;
		this.createdDt = createdDt;
		this.updatedDt = updatedDt;
	}
}