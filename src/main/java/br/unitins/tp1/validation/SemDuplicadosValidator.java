package br.unitins.tp1.validation;

import java.util.Collection;
import java.util.HashSet;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SemDuplicadosValidator implements ConstraintValidator<SemDuplicados, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return new HashSet<>(value).size() == value.size();
    }
}
