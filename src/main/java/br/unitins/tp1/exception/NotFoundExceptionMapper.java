package br.unitins.tp1.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        String detail = ProblemResponses.detailOf(exception, "O recurso solicitado nao foi encontrado.");
        return ProblemResponses.build(ProblemType.NOT_FOUND, detail, uriInfo);
    }
}
