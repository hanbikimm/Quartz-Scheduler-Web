package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final ResponseType type;

	public CommonException(ResponseType type) {
		super(type.getMessage());
        this.type = type;
	}
}