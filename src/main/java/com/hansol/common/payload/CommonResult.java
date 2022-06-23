package com.hansol.common.payload;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "payload.CommonResult (응답 결과)")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResult {
	
	@Schema(name = "성공 여부")
	private boolean success;
	
	@Schema(name = "응답 코드")
	private String code;
	
	@Schema(name = "응답 메시지")
	private String message;
	
	/**
     * 성공 응답 (응답 데이터 없음)
     */
    public static ResponseEntity<?> ofSuccess(ResponseType type) {
    	CommonResult response = new CommonResult(true, type.getCode(), type.getMessage());

        return new ResponseEntity<>(response, type.getStatus());
    }

    /**
     * 실패 응답 (응답 데이터 없음)
     */
    public static ResponseEntity<?> ofFailure(ResponseType type) {
    	CommonResult response = new CommonResult(false, type.getCode(), type.getMessage());

        return new ResponseEntity<>(response, type.getStatus());
    }
}