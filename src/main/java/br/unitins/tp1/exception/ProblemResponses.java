package br.unitins.tp1.exception;

import java.net.URI;
import java.util.List;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import jakarta.ws.rs.core.UriInfo;

/**
 * Monta as respostas application/problem+json usadas pelos ExceptionMappers.
 */
final class ProblemResponses {

    private static final URI ABOUT_BLANK = URI.create("about:blank");

    private ProblemResponses() {
    }

    static Response build(ProblemType type, String detail, UriInfo uriInfo, List<Violation> violations) {
        return build(type.getUri(), type.getTitle(), type.getStatus(), detail, uriInfo, violations);
    }

    /**
     * Para status sem tipo especifico usa "about:blank", em que o title e a
     * propria descricao do status HTTP (RFC 9457, secao 4.2.1).
     */
    static Response build(StatusType status, String detail, UriInfo uriInfo) {
        return build(ABOUT_BLANK, status.getReasonPhrase(), status, detail, uriInfo, null);
    }

    private static Response build(URI type, String title, StatusType status, String detail,
            UriInfo uriInfo, List<Violation> violations) {
        ProblemDetails problem = new ProblemDetails(
                type,
                title,
                status.getStatusCode(),
                detail,
                instance(uriInfo),
                violations);

        return Response.status(status)
                .type(ProblemDetails.MEDIA_TYPE)
                .entity(problem)
                .build();
    }

    static Response build(ProblemType type, String detail, UriInfo uriInfo) {
        return build(type, detail, uriInfo, null);
    }

    /**
     * Usa a mensagem da excecao como detail, ignorando a mensagem padrao do
     * JAX-RS ("HTTP 404 Not Found"), que nao acrescenta informacao ao title.
     */
    static String detailOf(WebApplicationException exception, String fallback) {
        String message = exception.getMessage();
        if (message == null || message.isBlank() || message.startsWith("HTTP ")) {
            return fallback;
        }
        return message;
    }

    private static URI instance(UriInfo uriInfo) {
        try {
            return URI.create(uriInfo.getPath());
        } catch (RuntimeException e) {
            return null;
        }
    }
}
