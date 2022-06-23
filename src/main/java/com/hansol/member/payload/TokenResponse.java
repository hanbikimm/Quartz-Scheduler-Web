package com.hansol.member.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
	private String token;
	
	public static TokenResponse of(String token) {
        return new TokenResponse(token);
    }
}