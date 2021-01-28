package net.lotte.lalpid.did.registrar.api.dto.validator.annotation;

import net.lotte.lalpid.did.registrar.api.dto.validator.UpdateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UpdateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateInputCheck {
    String message() default "Update form is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
