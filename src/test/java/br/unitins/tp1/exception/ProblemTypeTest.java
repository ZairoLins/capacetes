package br.unitins.tp1.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response.Status.Family;

class ProblemTypeTest {

    @Test
    void cadaTipoTemOStatusEsperado() {
        assertEquals(400, ProblemType.VALIDATION.getStatus().getStatusCode());
        assertEquals(422, ProblemType.BUSINESS_RULE.getStatus().getStatusCode());
        assertEquals(400, ProblemType.BAD_REQUEST.getStatus().getStatusCode());
        assertEquals(404, ProblemType.NOT_FOUND.getStatus().getStatusCode());
        assertEquals(409, ProblemType.CONFLICT.getStatus().getStatusCode());
        assertEquals(500, ProblemType.INTERNAL_ERROR.getStatus().getStatusCode());
    }

    @Test
    void cadaTipoTemUriETitulo() {
        assertEquals(URI.create("/problems/validation-error"), ProblemType.VALIDATION.getUri());
        assertEquals("Erro de validacao", ProblemType.VALIDATION.getTitle());
        assertEquals(URI.create("/problems/conflict"), ProblemType.CONFLICT.getUri());
        assertEquals("Conflito de integridade dos dados", ProblemType.CONFLICT.getTitle());
    }

    @Test
    void status422EErroDoCliente() {
        assertEquals(Family.CLIENT_ERROR, ProblemType.BUSINESS_RULE.getStatus().getFamily());
        assertEquals("Unprocessable Content", ProblemType.BUSINESS_RULE.getStatus().getReasonPhrase());
    }
}
