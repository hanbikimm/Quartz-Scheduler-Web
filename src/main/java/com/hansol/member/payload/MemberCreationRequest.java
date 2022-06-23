package com.hansol.member.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "member.payload.MemberCreationRequest (회원 가입)")
public class MemberCreationRequest {
	
	@Schema(example = "12345")
	// @NotBlank(message = "사번을 입력하세요.")
	@Pattern(regexp = "^[0-9]{4,10}$", message = "사번 형식이 맞지 않습니다.")
	private String username;
	
	@Schema(example = "12345test")
	// @NotBlank(message = "비밀번호를 입력하세요.")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$", message = "8~16자 이내에서 영문 대소문자, 숫자를 사용하세요.")		   
	private String password;
	
	@Schema(example = "12345@test.com")
	// @NotBlank(message = "이메일을 입력하세요.")
	@Email(message = "이메일 형식이 맞지 않습니다.")
	private String email;
	
	@Size(max = 30, message = "이름은 30자 이내로 입력하세요.")
	@Schema(example = "test")
	@NotBlank(message = "이름을 입력하세요.")
	private String name;

	@Builder
	public MemberCreationRequest(String username, String password, String email, String name) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
	}
}