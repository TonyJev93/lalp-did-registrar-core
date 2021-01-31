package net.lotte.lalpid.did.registrar.api.dto.validator;

import com.auth0.jwt.JWT;
import foundation.identity.did.DIDURL;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import net.lotte.lalpid.did.registrar.infrastructure.util.DID.DIDUrlUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_TOKEN_DID_NOT_EQUALS_WITH_PATH_DID;

@Component
public class TokenValidator {
    public boolean validate(String token, String didFromPathVariable, BindingResult bindingResult) {
        // get did url
        DIDURL didUrlFromToken = DIDUrlUtil.fromString(JWT.decode(token).getKeyId());

        // Path Variable's DID == Jwt's DID 일치 여부 확인
        if (!didFromPathVariable.equals(didUrlFromToken.getDid().getDidString())) {
            FieldError fieldError = new FieldError("req", "token", VALID_CHK_MSG_TOKEN_DID_NOT_EQUALS_WITH_PATH_DID);
            bindingResult.addError(fieldError);

            try {
                throw new BindException(bindingResult);
            } catch (BindException e) {
                throw new BusinessException("Binding Exception", ErrorCode.INVALID_INPUT_VALUE);
            }
        }
        return true;
    }
}
