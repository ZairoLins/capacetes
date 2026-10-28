package br.unitins.tp1.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

@ExtendWith(MockitoExtension.class)
class ProblemResponsesTest {

    @Mock
    UriInfo uriInfo;

    @Test
    void buildComProblemTypeMontaOCorpoCompleto() {
        when(uriInfo.getPath()).thenReturn("/cores");
        List<Violation> violations = List.of(new Violation("nome", "Obrigatorio."));

        Response response = ProblemResponses.build(ProblemType.VALIDATION, "Detalhe", uriInfo, violations);

        assertEquals(400, response.getStatus());
        assertEquals(ProblemDetails.MEDIA_TYPE, response.getMediaType().toString());
        assertEquals(new ProblemDetails(URI.create("/problems/validation-error"), "Erro de validacao", 400,
                "Detalhe", URI.create("/cores"), violations), response.getEntity());
    }

    @Test
    void buildSemViolacoesDeixaViolationsNulo() {
        when(uriInfo.getPath()).thenReturn("/cores/1");

        Response response = ProblemResponses.build(ProblemType.NOT_FOUND, "Nao achou", uriInfo);

        assertNull(((ProblemDetails) response.getEntity()).violations());
    }

    @Test
    void buildComStatusUsaAboutBlankEAFraseDoStatus() {
        when(uriInfo.getPath()).thenReturn("/cores/1");

        Response response = ProblemResponses.build(Status.METHOD_NOT_ALLOWED, null, uriInfo);

        ProblemDetails problem = (ProblemDetails) response.getEntity();
        assertEquals(405, response.getStatus());
        assertEquals(URI.create("about:blank"), problem.type());
        assertEquals("Method Not Allowed", problem.title());
        assertEquals(405, problem.status());
        assertNull(problem.detail());
    }

    @Test
    void instanceFicaNuloQuandoOCaminhoNaoEUmaUriValida() {
        when(uriInfo.getPath()).thenReturn("/cores/com espaco");

        Response response = ProblemResponses.build(ProblemType.BAD_REQUEST, "x", uriInfo);

        assertNull(((ProblemDetails) response.getEntity()).instance());
    }

    @Test
    void instanceFicaNuloQuandoNaoHaUriInfo() {
        Response response = ProblemResponses.build(ProblemType.BAD_REQUEST, "x", null);

        assertNull(((ProblemDetails) response.getEntity()).instance());
    }

    @Test
    void detailOfUsaAMensagemDaExcecao() {
        assertEquals("Cor nao encontrada.",
                ProblemResponses.detailOf(new NotFoundException("Cor nao encontrada."), "padrao"));
    }

    @Test
    void detailOfIgnoraAMensagemPadraoDoJaxRs() {
        // a mensagem padrao e "HTTP 404 Not Found"
        assertEquals("padrao", ProblemResponses.detailOf(new NotFoundException(), "padrao"));
    }

    @Test
    void detailOfIgnoraMensagemEmBranco() {
        assertEquals("padrao", ProblemResponses.detailOf(new NotFoundException(" "), "padrao"));
    }

    @Test
    void problemDetailsOmiteCamposNulosNoJson() throws Exception {
        ProblemDetails problem = new ProblemDetails(URI.create("/problems/not-found"), "Recurso nao encontrado",
                404, "Cor nao encontrada.", null, null);

        String json = new ObjectMapper().writeValueAsString(problem);

        assertEquals("{\"type\":\"/problems/not-found\",\"title\":\"Recurso nao encontrado\","
                + "\"status\":404,\"detail\":\"Cor nao encontrada.\"}", json);
    }
}
