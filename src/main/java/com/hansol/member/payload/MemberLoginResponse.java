package com.hansol.member.payload;

import com.hansol.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginResponse {
	private String username;
	private String name;
	private String email;
	private String token;

	@Builder
	public MemberLoginResponse(String username, String name, String email, String token) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.token = token;
	}

	public static MemberLoginResponse of(Member member, String token) {
		return MemberLoginResponse.builder()
				.username(member.getUsername())
				.name(member.getName())
				.email(member.getEmail())
				.token(token).build();
	}
}