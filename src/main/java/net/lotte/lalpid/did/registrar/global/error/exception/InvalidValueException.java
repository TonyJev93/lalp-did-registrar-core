package net.lotte.lalpid.did.registrar.global.error.exception;

import net.lotte.lalpid.did.registrar.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
