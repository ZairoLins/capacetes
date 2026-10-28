package br.unitins.tp1.exception;

import java.net.URI;

import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.Response.StatusType;

/**
 * Tipos de problema conhecidos pela API. O type e uma URI relativa que
 * identifica a categoria do erro, conforme a RFC 9457.
 */
public enum ProblemType {
    VALIDATION("/problems/validation-error", "Erro de validacao", Status.BAD_REQUEST),
    BUSINESS_RULE("/problems/business-rule", "Regra de negocio violada", UnprocessableContent.STATUS),
    BAD_REQUEST("/problems/bad-request", "Requisicao invalida", Status.BAD_REQUEST),
    NOT_FOUND("/problems/not-found", "Recurso nao encontrado", Status.NOT_FOUND),
    CONFLICT("/problems/conflict", "Conflito de integridade dos dados", Status.CONFLICT),
    INTERNAL_ERROR("/problems/internal-error", "Erro interno do servidor", Status.INTERNAL_SERVER_ERROR);

    private final URI uri;
    private final String title;
    private final StatusType status;

    ProblemType(String uri, String title, StatusType status) {
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

    public StatusType getStatus() {
        return status;
    }

    // 422 nao existe no enum Status do Jakarta REST 3.1
    private static final class UnprocessableContent {
        static final StatusType STATUS = new StatusType() {
            @Override
            public int getStatusCode() {
                return 422;
            }

            @Override
            public Family getFamily() {
                return Family.CLIENT_ERROR;
            }

            @Override
            public String getReasonPhrase() {
                return "Unprocessable Content";
            }
        };
    }
}
