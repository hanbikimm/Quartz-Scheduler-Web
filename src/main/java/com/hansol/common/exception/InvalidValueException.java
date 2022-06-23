package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class InvalidValueException extends CommonException {
	private static final long serialVersionUID = 1L;

    public InvalidValueException(ResponseType type) {
        super(type);
    }
}