package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class AuthorizationException extends CommonException {
	private static final long serialVersionUID = 1L;

	public AuthorizationException(ResponseType type) {
        super(type);
    }
}