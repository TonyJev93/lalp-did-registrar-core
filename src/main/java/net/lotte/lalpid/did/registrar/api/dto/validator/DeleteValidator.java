package net.lotte.lalpid.did.registrar.api.dto.validator;

import net.lotte.lalpid.did.registrar.api.dto.RegistrarDto;
import net.lotte.lalpid.did.registrar.api.dto.validator.annotation.DeleteInputCheck;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class DeleteValidator implements ConstraintValidator<DeleteInputCheck, RegistrarDto.DeleteReq> {
    @Override
    public void initialize(DeleteInputCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrarDto.DeleteReq req, ConstraintValidatorContext constraintValidatorContext) {
        int invalidCount = 0;
        req.getDid();
        if (Optional.ofNullable(req.getDid()).orElseGet(() -> "0").equals("1")) {
            addConstraintViolation(constraintValidatorContext, "Not found did error", "did");

            invalidCount += 1;
        }

        return invalidCount == 0;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage, String firstNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(firstNode)
                .addConstraintViolation();
    }
}
