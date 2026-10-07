package br.unitins.tp1.exception;

import java.util.List;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException exception) {
        List<Violation> violations = exception.getField() == null
                ? null
                : List.of(new Violation(exception.getField(), exception.getMessage()));

        return ProblemResponses.build(ProblemType.CONFLICT, exception.getMessage(), uriInfo, violations);
    }
}
