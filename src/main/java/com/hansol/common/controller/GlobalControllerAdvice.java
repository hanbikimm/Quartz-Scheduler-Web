package com.hansol.common.controller;

import java.time.DateTimeException;
import javax.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.hansol.common.exception.AuthenticationException;
import com.hansol.common.exception.AuthorizationException;
import com.hansol.common.exception.CommonException;
import com.hansol.common.exception.DuplicateValueException;
import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.exception.ValueNotFoundException;
import com.hansol.common.payload.CommonResult;
import com.hansol.common.payload.ResourceResult;
import com.hansol.common.payload.ResponseType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
	
	/**
     * 잘못된 값을 요청하는 경우 (400)
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidValueException.class})
    public ResponseEntity<?> handleException(InvalidValueException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return CommonResult.ofFailure(e.getType());
    }

    /**
     * 유효하지 않은 값을 요청하는 경우 (400)
     * - BindException 클래스: @ModelAttribute, @Valid 어노테이션 관련
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<?> handleValidationException(BindException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return ResourceResult.ofFailure(e.getBindingResult());
    }
    
    /**
     * 유효하지 않은 값을 요청하는 경우 (400)
     * - MethodArgumentNotValidException 클래스: @RequestBody, @Valid 어노테이션 관련
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return ResourceResult.ofFailure(e.getBindingResult());
    }

    /**
     * 유효하지 않은 값을 요청하는 경우 (400)
     * - MethodArgumentTypeMismatchException 클래스: Enum 클래스 관련
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleValidationException(MethodArgumentTypeMismatchException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return ResourceResult.ofFailure(e);
    }
    
    /**
     * 유효하지 않은 값을 요청하는 경우 (400)
     * - HttpMessageNotReadableException 클래스: @RequestBody, JSON 값 관련
     * + HttpMessageNotWritableException 클래스: @ResponseBody, JSON 값 관련
     * - DateTimeException 클래스: 날짜/시간 관련 최상위 클래스
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = {HttpMessageNotReadableException.class})
	private ResponseEntity<?> handleMeesageException(HttpMessageNotReadableException e) {
    	ResponseType type = ResponseType.INVALID_VALUE;
    	
    	Throwable cause = e.getRootCause();
    	if (cause instanceof DateTimeException) type = ResponseType.INVALID_DATE_VALUE;
		
		log.info("[INFO] [400] 예외 처리: {}", e.getMessage());
		return CommonResult.ofFailure(type);
	}

    /**
     * 존재하지 않는 값을 요청하는 경우 (404)
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ValueNotFoundException.class})
    public ResponseEntity<?> handleException(ValueNotFoundException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return CommonResult.ofFailure(e.getType());
    }

    /**
     * 이미 존재하는 값을 생성 요청하는 경우 (409)
     */
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {DuplicateValueException.class})
    public ResponseEntity<?> handleException(DuplicateValueException e) {
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return CommonResult.ofFailure(e.getType());
    }
    
    /**
     * 인증/인가 없이 요청하는 경우 (401, 403)
     */
    @ExceptionHandler(value = {AuthenticationException.class, AuthorizationException.class, AccessDeniedException.class})
    public ResponseEntity<?> handleSecurityException(RuntimeException e) {
    	ResponseType type = ResponseType.DETAULT_SERVER_ERROR;
    	if (e instanceof AuthenticationException) type = ((AuthenticationException) e).getType();
		if (e instanceof AuthorizationException) type = ((AuthorizationException) e).getType();
		if (e instanceof AccessDeniedException) type = ResponseType.UNAUTHORIZED;
    	
    	log.info("[INFO] 예외 처리: {}", e.getMessage());
        return CommonResult.ofFailure(type);
    }

    /**
     * 제공하지 않는 API 경로 또는 HTTP 메소드를 요청하는 경우 (405)
     */
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleServletException(ServletException e) {
        ResponseType type = ResponseType.DETAULT_SERVER_ERROR;
        if (e instanceof NoHandlerFoundException) type = ResponseType.API_NOT_ALLOWED;
        if (e instanceof HttpRequestMethodNotSupportedException) type = ResponseType.METHOD_NOT_ALLOWED;

        log.info("[INFO] 예외 처리: {}", e.getMessage());
        return CommonResult.ofFailure(type);
    }

    /**
     * 서버 내부에서 오류가 발생하는 경우 (500)
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {CommonException.class, RuntimeException.class})
    public ResponseEntity<?> handleServerException(RuntimeException e) {
        ResponseType type = ResponseType.DETAULT_SERVER_ERROR;

        log.info("[INFO] 예외 처리: {}", e.getMessage());
        // 위의 핸들러에서 잡지 못한 CommonException 클래스의 자식 클래스를 처리하기 위해 분기 처리하였음.
        // CommonException 클래스인 경우, ResponseType 클래스에 정의한 메시지를 사용하도록 하였음.
        // RuntimeException 클래스인 경우, 자동으로 세팅되는 오류 메시지를 클라이언트에 전달하지 않기 위해 type 객체를 전달하였음.
        return (e instanceof CommonException)
                ? CommonResult.ofFailure(((CommonException) e).getType())
                : CommonResult.ofFailure(type);
    }
}