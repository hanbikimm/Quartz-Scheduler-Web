package com.hansol.common.payload;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseType {
	
	/* 200 (OK) */
	OK(HttpStatus.OK, "COM-001", "요청을 수행하였습니다."),
	
	/* 400 (Bad Request) */
	INVALID_VALUE(HttpStatus.BAD_REQUEST, "COM-002", "잘못된 값이 요청되었습니다."),
	INVALID_DATE_VALUE(HttpStatus.BAD_REQUEST, "COM-003", "잘못된 날짜/시간입니다. 알맞은 형식으로 입력해주세요!"),
	INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "COM-004", "잘못된 선택 값이 요청되었습니다."),
    MEMBER_LOGIN_FAILURE(HttpStatus.BAD_REQUEST, "MEM-001", "아이디와 비밀번호를 확인해주세요."),
    INVALID_SIMPLE_TRIGGER(HttpStatus.BAD_REQUEST, "JOB-001", "잘못된 SIMPLE 작업입니다. 다시 입력해주세요."),
    INVALID_CRON_TRIGGER(HttpStatus.BAD_REQUEST, "JOB-002", "잘못된 CRON 표현식입니다. 다시 입력해주세요."),
    INVALID_TRIGGER_DATE(HttpStatus.BAD_REQUEST, "JOB-003", "잘못된 시작(종료) 시간입니다.\n현재 시간보다 이후로 설정해주세요!"),
    
	/* 401 (Unauthorized) */
	UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "COM-005", "로그인이 필요합니다!"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "COM-006", "세션이 만료되었습니다. 다시 로그인해주세요!"),
    
	/* 403 (Forbidden) */
	UNAUTHORIZED(HttpStatus.FORBIDDEN, "COM-007", "요청을 수행하기 위한 접근 권한이 없습니다."),
	
	/* 404 (Not Found) */
	VALUE_NOT_FOUND(HttpStatus.NOT_FOUND, "COM-008", "요청된 값을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEM-002", "해당 계정을 찾을 수 없습니다."),
    JOB_LABEL_NOT_FOUND(HttpStatus.NOT_FOUND, "JOB-004", "해당 스케줄 정보를 찾을 수 없습니다."),
    JOB_CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "JOB-005", "해당 스케줄 클래스를 찾을 수 없습니다."),
    JOB_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "JOB-006", "해당 스케줄의 로그 정보를 찾을 수 없습니다."),
    UNSUPPORTED_TRIGGER(HttpStatus.NOT_FOUND, "JOB-007", "해당 스케줄에 대한 트리거를 찾을 수 없습니다."),
    
	/* 405 (Method Not Allowed) */
	API_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COM-009", "제공하지 않는 API 경로입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COM-010", "제공하지 않는 HTTP 요청입니다."),
    
	/* 409 (Conflict) */
	DUPLICATE_VALUE(HttpStatus.CONFLICT, "COM-011", "요청된 값이 이미 존재합니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "MEM-003", "이미 등록된 사번은 사용하실 수 없습니다. 다시 입력해주세요!"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "MEM-004", "이미 등록된 이메일은 사용하실 수 없습니다. 다시 입력해주세요!"),
    DUPLICATE_SCHEDULE(HttpStatus.CONFLICT, "JOB-008", "이미 등록된 이름의 스케줄입니다.\n그룹 내에 같은 이름이 있는지 확인해주세요!"),
    
	/* 500 (Internal Server Error) */
	DETAULT_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COM-012", "서버 오류가 발생하였습니다."),
    SCHEDULE_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JOB-009", "스케줄을 등록하지 못하였습니다."),
    SCHEDULE_SELECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JOB-010", "스케줄을 조회하지 못하였습니다."),
    SCHEDULE_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JOB-011", "스케줄을 수정하지 못하였습니다."),
    SCHEDULE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JOB-012", "스케줄을 삭제하지 못하였습니다.");
	
    private final HttpStatus status;
    private final String code;
    private final String message;
    
	private static final Map<String, ResponseType> codes = Collections
	        .unmodifiableMap(Stream.of(values())
	        .collect(Collectors.toMap(ResponseType::getCode, Function.identity())));
	
	public static ResponseType of(String value) {
	    return Optional
	            .ofNullable(codes.get(value))
	            .orElse(DETAULT_SERVER_ERROR);
	}
}