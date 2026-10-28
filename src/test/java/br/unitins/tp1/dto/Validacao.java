package br.unitins.tp1.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Valida os DTOs com o Hibernate Validator, sem subir o Quarkus. As mensagens
 * dos DTOs sao texto fixo, entao o interpolador sem Expression Language basta.
 */
final class Validacao {

    private static final Validator VALIDATOR = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private Validacao() {
    }

    /** Violacoes agrupadas por campo (propertyPath) */
    static Map<String, Set<String>> violacoes(Object dto) {
        Map<String, Set<String>> porCampo = new TreeMap<>();
        for (ConstraintViolation<Object> v : VALIDATOR.validate(dto)) {
            porCampo.computeIfAbsent(v.getPropertyPath().toString(), k -> new TreeSet<>()).add(v.getMessage());
        }
        return porCampo;
    }

    static void assertValido(Object dto) {
        assertEquals(Map.of(), violacoes(dto));
    }

    /** O DTO deve ter apenas as mensagens informadas, todas no campo informado */
    static void assertSomente(Object dto, String campo, String... mensagens) {
        assertEquals(Map.of(campo, Set.of(mensagens)), violacoes(dto));
    }
}
