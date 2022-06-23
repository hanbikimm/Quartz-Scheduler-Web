package com.hansol.member.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum RoleType {
	DEFAULT, UNKNOWN;
	
	private static final Map<String, RoleType> names = Collections
	        .unmodifiableMap(Stream.of(values())
	        .collect(Collectors.toMap(RoleType::name, Function.identity())));

	@JsonCreator
	public static RoleType of(String value) {
	    return Optional
	            .ofNullable(names.get(value))
	            .orElse(UNKNOWN);
	}
}