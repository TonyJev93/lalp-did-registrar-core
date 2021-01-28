package net.lotte.lalpid.did.registrar.api.dto.validator;

import foundation.identity.did.DIDURL;
import net.lotte.lalpid.did.registrar.api.dto.RegistrarDto;
import net.lotte.lalpid.did.registrar.api.dto.validator.annotation.UpdateInputCheck;
import net.lotte.lalpid.did.registrar.domain.Token;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_TOKEN_DID_NOT_EQUALS_WITH_PATH_DID;

@Component
public class UpdateValidator implements ConstraintValidator<UpdateInputCheck, RegistrarDto.UpdateReq> {
    @Override
    public void initialize(UpdateInputCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrarDto.UpdateReq req, ConstraintValidatorContext constraintValidatorContext) {
        int invalidCount = 0;

        return invalidCount == 0;
    }

    public boolean validate(RegistrarDto.UpdateReq req, String didUrlFromPathVariable, BindingResult bindingResult) throws BindException {
        DIDURL didUrlFromToken = null;

        didUrlFromToken = Token.getDidUrl(req.getToken());

        // Path Variable's DID == Token's DID 여부 확인
        if (!didUrlFromPathVariable.equals(didUrlFromToken.getDid().getDidString())) {
            FieldError fieldError = new FieldError("req", "token", VALID_CHK_MSG_TOKEN_DID_NOT_EQUALS_WITH_PATH_DID);
            bindingResult.addError(fieldError);
            throw new BindException(bindingResult);
        }

        return true;
    }

}
