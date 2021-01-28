package net.lotte.lalpid.did.registrar.api.dto.validator.annotation;

import net.lotte.lalpid.did.registrar.api.dto.validator.DeleteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeleteValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteInputCheck {
    String message() default "Delete Form is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
