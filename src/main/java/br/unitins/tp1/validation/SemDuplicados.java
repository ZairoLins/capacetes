package br.unitins.tp1.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * A colecao nao pode conter elementos repetidos. Colecoes nulas sao validas.
 */
@Documented
@Constraint(validatedBy = SemDuplicadosValidator.class)
@Target({ FIELD, PARAMETER, RECORD_COMPONENT })
@Retention(RUNTIME)
public @interface SemDuplicados {

    String message() default "A lista nao pode conter itens repetidos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
