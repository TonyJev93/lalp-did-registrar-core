package net.lotte.lalpid.did.registrar.api.dto.validator.annotation;

import net.lotte.lalpid.did.registrar.api.dto.validator.RegisterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegisterValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterInputCheck {
    String message() default "Register Form is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
