package meu.projeto.loja.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Documented
public @interface NotBlankIfPresent {
    String message() default "Campo não pode ser vazio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
