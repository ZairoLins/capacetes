package br.unitins.tp1.exception;

import java.util.Comparator;
import java.util.List;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Violation> violations = exception.getConstraintViolations().stream()
                .map(v -> new Violation(fieldName(v), v.getMessage()))
                .sorted(Comparator.comparing(Violation::field).thenComparing(Violation::message))
                .toList();

        return ProblemResponses.build(ProblemType.VALIDATION,
                "Um ou mais campos possuem valores invalidos.", uriInfo, violations);
    }

    /**
     * Converte o propertyPath (ex.: "inserir.dto.especificacao.peso") no nome
     * do campo como o cliente o enviou (ex.: "especificacao.peso"), removendo
     * os nos de metodo e de parametro. Itens de lista aparecem com o indice,
     * ex.: "idFornecedores[1]".
     */
    private String fieldName(ConstraintViolation<?> violation) {
        StringBuilder field = new StringBuilder();
        String parameter = null;

        for (Path.Node node : violation.getPropertyPath()) {
            if (node.getKind() == ElementKind.METHOD) {
                continue;
            }
            if (node.getKind() == ElementKind.PARAMETER) {
                parameter = node.getName();
                continue;
            }
            if (node.isInIterable() && !field.isEmpty()) {
                field.append('[').append(node.getIndex() != null ? node.getIndex() : node.getKey()).append(']');
            }
            // o no do elemento da lista tem um nome generico ("<list element>")
            if (node.getKind() == ElementKind.CONTAINER_ELEMENT || node.getName() == null) {
                continue;
            }
            if (!field.isEmpty()) {
                field.append('.');
            }
            field.append(node.getName());
        }

        if (!field.isEmpty()) {
            return field.toString();
        }
        return parameter != null ? parameter : violation.getPropertyPath().toString();
    }
}
