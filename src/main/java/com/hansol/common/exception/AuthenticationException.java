package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class AuthenticationException extends CommonException {
	private static final long serialVersionUID = 1L;

	public AuthenticationException(ResponseType type) {
        super(type);
    }
}