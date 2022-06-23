package com.hansol.member.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "member.payload.MemberLoginRequest (회원 로그인)")
public class MemberLoginRequest {
	
	@JsonProperty("username")
	@Schema(example = "12345")
	private String usernameOrEmail;
	
	@Schema(example = "12345test")
	private String password;
	
	@Builder
	public MemberLoginRequest(String usernameOrEmail, String password) {
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
	}
}