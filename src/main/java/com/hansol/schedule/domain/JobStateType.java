package com.hansol.schedule.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum JobStateType {
	// JOB_LABEL용
	USING, TERMINATED, DELETED,

	// QUARTZ에서 가져온 상태 표시용
	SCHEDULED, RUNNING, PAUSED, RESUMED,
	
	// Enum 유효성 검사 시, 매핑되지 않은 경우 리턴하는 값입니다.
	UNKNOWN;
	
	private static final Map<String, JobStateType> names = Collections
	        .unmodifiableMap(Stream.of(values())
	        .collect(Collectors.toMap(JobStateType::name, Function.identity())));

	@JsonCreator
	public static JobStateType of(String value) {
	    return Optional
	            .ofNullable(names.get(value))
	            .orElse(UNKNOWN);
	}
}