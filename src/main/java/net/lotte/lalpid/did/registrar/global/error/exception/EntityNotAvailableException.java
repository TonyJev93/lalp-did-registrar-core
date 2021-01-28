package net.lotte.lalpid.did.registrar.global.error.exception;

import net.lotte.lalpid.did.registrar.global.error.ErrorCode;

public class EntityNotAvailableException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public EntityNotAvailableException(String value) {
        super(value, ErrorCode.ENTITY_NOT_AVAILABLE);
    }

    public EntityNotAvailableException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
