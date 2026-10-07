package br.unitins.tp1.exception;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Corpo de resposta de erro no formato Problem Details (RFC 9457).
 * O campo violations e uma extensao usada apenas em erros de validacao.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
    URI type,
    String title,
    int status,
    String detail,
    URI instance,
    List<Violation> violations) {

    public static final String MEDIA_TYPE = "application/problem+json";

    public record Violation(String field, String message) {
    }

}
