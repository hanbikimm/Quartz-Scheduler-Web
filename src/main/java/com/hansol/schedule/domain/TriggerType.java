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
public enum TriggerType {
	SIMPLE, CRON, UNKNOWN;
	
	private static final Map<String, TriggerType> names = Collections
	        .unmodifiableMap(Stream.of(values())
	        .collect(Collectors.toMap(TriggerType::name, Function.identity())));

	@JsonCreator
	public static TriggerType of(String value) {
	    return Optional
	            .ofNullable(names.get(value))
	            .orElse(UNKNOWN);
	}
}