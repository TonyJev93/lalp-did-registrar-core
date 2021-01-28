package net.lotte.lalpid.did.registrar.global.error.exception;

import net.lotte.lalpid.did.registrar.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
