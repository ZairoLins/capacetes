package br.unitins.tp1.exception;

import java.util.List;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessValidationExceptionMapper implements ExceptionMapper<BusinessValidationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(BusinessValidationException exception) {
        List<Violation> violations = List.of(new Violation(exception.getField(), exception.getMessage()));

        return ProblemResponses.build(ProblemType.BUSINESS_RULE,
                "A requisicao viola uma regra de negocio.", uriInfo, violations);
    }
}
