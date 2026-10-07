package br.unitins.tp1.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Trata valores com tipo incompativel no JSON (ex.: texto em um campo numerico),
 * substituindo o mapper padrao do Quarkus para manter o formato Problem Details.
 */
@Provider
public class MismatchedInputExceptionMapper implements ExceptionMapper<MismatchedInputException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(MismatchedInputException exception) {
        String field = exception.getPath().stream()
                .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[" + ref.getIndex() + "]")
                .collect(Collectors.joining("."))
                .replace(".[", "[");

        if (field.isEmpty()) {
            return ProblemResponses.build(ProblemType.BAD_REQUEST,
                    "O corpo da requisicao nao pode ser interpretado.", uriInfo);
        }

        List<Violation> violations = List.of(new Violation(field, "Valor com tipo incompativel."));
        return ProblemResponses.build(ProblemType.BAD_REQUEST,
                "Um ou mais campos possuem tipo incompativel.", uriInfo, violations);
    }
}
