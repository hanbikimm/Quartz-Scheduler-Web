package com.hansol.member.payload;

import com.hansol.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberProfileResponse {
	private String username;
	private String name;
	private String email;
	
	@Builder
	public MemberProfileResponse(String username, String name, String email) {
		this.username = username;
		this.name = name;
		this.email = email;
	}
	
	public static MemberProfileResponse of(Member member) {
		return MemberProfileResponse.builder()
				.username(member.getUsername())
				.name(member.getName())
				.email(member.getEmail())
				.build();
	}
}