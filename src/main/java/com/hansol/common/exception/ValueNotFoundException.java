package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class ValueNotFoundException extends CommonException {
	private static final long serialVersionUID = 1L;

    public ValueNotFoundException(ResponseType type) {
        super(type);
    }
}