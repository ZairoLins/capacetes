package br.unitins.tp1.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Fallback para as demais excecoes HTTP (JSON malformado, 405, 415, ...).
 * BadRequestException e NotFoundException possuem mappers proprios, que tem
 * prioridade por serem mais especificos.
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        StatusType status = exception.getResponse().getStatusInfo();

        if (status.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode()) {
            String detail = ProblemResponses.detailOf(exception, "O corpo da requisicao nao pode ser interpretado.");
            return ProblemResponses.build(ProblemType.BAD_REQUEST, detail, uriInfo);
        }

        return ProblemResponses.build(status, ProblemResponses.detailOf(exception, null), uriInfo);
    }
}
