package com.hansol.common.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "payload.ResourceResult (응답 결과)")
public class ResourceResult extends CommonResult {

	@Schema(name = "응답 데이터")
	private Object result;
	
	private ResourceResult(boolean success, ResponseType type, Object result) {
		super(success, type.getCode(), type.getMessage());
		this.result = result;
	}
	
	 /**
     * 성공 응답 (응답 데이터 있음)
     */
    public static ResponseEntity<?> ofSuccess(ResponseType type, Object result) {
    	ResourceResult response = new ResourceResult(true, type, result);

        return new ResponseEntity<>(response, type.getStatus());
    }
    
    /**
     * 실패 응답 (응답 데이터 있음)
     * - @ModelAttribute, @Valid / @RequestBody, @Valid 어노테이션으로 인한 오류 응답 데이터를 포함한다.
     */
    public static ResponseEntity<?> ofFailure(BindingResult result) {
        ResponseType type = ResponseType.INVALID_VALUE;
        ResourceResult response = new ResourceResult(false, type, FieldError.of(result));

        return new ResponseEntity<>(response, type.getStatus());
    }
    
    /**
     * 실패 응답 (응답 데이터 있음)
     * - Enum 클래스로 인한 오류 응답 데이터를 포함한다.
     */
    public static ResponseEntity<?> ofFailure(MethodArgumentTypeMismatchException e) {
        String name = e.getName();
        String value = e.getValue() == null ? "" : e.getValue().toString();
        String code = e.getErrorCode();

        ResponseType type = ResponseType.INVALID_VALUE;
        ResourceResult response = new ResourceResult(false, type, FieldError.of(name, value, code));

        return new ResponseEntity<>(response, type.getStatus());
    }
    
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FieldError {
        private String field;
        private String value;
        private String reason;

        private static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> errors = new ArrayList<>();
            errors.add(new FieldError(field, value, reason));

            return errors;
        }

        private static List<FieldError> of(BindingResult result) {
            List<org.springframework.validation.FieldError> errors = result.getFieldErrors();

            return errors.stream()
                    .map(error -> new FieldError(
                    		error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}