package br.unitins.tp1.exception;

import java.net.URI;

import jakarta.ws.rs.core.Response.Status;

/**
 * Tipos de problema conhecidos pela API. O type e uma URI relativa que
 * identifica a categoria do erro, conforme a RFC 9457.
 */
public enum ProblemType {
    VALIDATION("/problems/validation-error", "Erro de validacao", Status.BAD_REQUEST),
    BAD_REQUEST("/problems/bad-request", "Requisicao invalida", Status.BAD_REQUEST),
    NOT_FOUND("/problems/not-found", "Recurso nao encontrado", Status.NOT_FOUND),
    CONFLICT("/problems/conflict", "Conflito com o estado atual do recurso", Status.CONFLICT);

    private final URI uri;
    private final String title;
    private final Status status;

    ProblemType(String uri, String title, Status status) {
        this.uri = URI.create(uri);
        this.title = title;
        this.status = status;
    }

    public URI getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }
}
