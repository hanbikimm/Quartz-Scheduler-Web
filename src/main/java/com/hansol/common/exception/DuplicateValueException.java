package com.hansol.common.exception;

import com.hansol.common.payload.ResponseType;
import lombok.Getter;

@Getter
public class DuplicateValueException extends CommonException {
	private static final long serialVersionUID = 1L;

	public DuplicateValueException(ResponseType type) {
        super(type);
    }
}