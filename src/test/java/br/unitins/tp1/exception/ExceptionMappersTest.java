package br.unitins.tp1.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import br.unitins.tp1.exception.ProblemDetails.Violation;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@ExtendWith(MockitoExtension.class)
class ExceptionMappersTest {

    @Mock
    UriInfo uriInfo;

    @BeforeEach
    void caminhoDaRequisicao() {
        lenient().when(uriInfo.getPath()).thenReturn("/recurso");
    }

    private static ProblemDetails problem(Response response) {
        assertEquals(ProblemDetails.MEDIA_TYPE, response.getMediaType().toString());
        ProblemDetails problem = (ProblemDetails) response.getEntity();
        assertEquals(response.getStatus(), problem.status());
        assertEquals(URI.create("/recurso"), problem.instance());
        return problem;
    }

    // NotFoundExceptionMapper

    @Test
    void notFoundUsaAMensagemDoService() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new NotFoundException("Cor nao encontrada.")));

        assertEquals(404, problem.status());
        assertEquals(URI.create("/problems/not-found"), problem.type());
        assertEquals("Recurso nao encontrado", problem.title());
        assertEquals("Cor nao encontrada.", problem.detail());
    }

    @Test
    void notFoundSemMensagemUsaDetalhePadrao() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new NotFoundException()));

        assertEquals("O recurso solicitado nao foi encontrado.", problem.detail());
    }

    // BadRequestExceptionMapper

    @Test
    void badRequestUsaAMensagemDaExcecao() {
        BadRequestExceptionMapper mapper = new BadRequestExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new BadRequestException("Tamanho invalido: 9")));

        assertEquals(400, problem.status());
        assertEquals(URI.create("/problems/bad-request"), problem.type());
        assertEquals("Tamanho invalido: 9", problem.detail());
    }

    @Test
    void badRequestSemMensagemUsaDetalhePadrao() {
        BadRequestExceptionMapper mapper = new BadRequestExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new BadRequestException()));

        assertEquals("A requisicao enviada e invalida.", problem.detail());
    }

    // BusinessValidationExceptionMapper

    @Test
    void regraDeNegocioRetorna422ComOCampo() {
        BusinessValidationExceptionMapper mapper = new BusinessValidationExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(
                new BusinessValidationException("quantidade", "Estoque insuficiente.")));

        assertEquals(422, problem.status());
        assertEquals(URI.create("/problems/business-rule"), problem.type());
        assertEquals("A requisicao viola uma regra de negocio.", problem.detail());
        assertEquals(List.of(new Violation("quantidade", "Estoque insuficiente.")), problem.violations());
    }

    // GlobalExceptionMapper

    @ParameterizedTest
    @CsvSource({
        "23503, O registro esta vinculado a outros registros e a operacao nao pode ser concluida.",
        "23505, Ja existe um registro com os dados informados.",
        "23502, A operacao viola uma restricao de integridade dos dados.",
    })
    void violacaoDeIntegridadeDoBancoRetorna409(String sqlState, String detalhe) {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper();
        mapper.uriInfo = uriInfo;
        // o erro do banco chega embrulhado em outras excecoes
        Exception erro = new RuntimeException("transacao",
                new PersistenceException("hibernate", new SQLException("detalhes do banco", sqlState)));

        ProblemDetails problem = problem(mapper.toResponse(erro));

        assertEquals(409, problem.status());
        assertEquals(URI.create("/problems/conflict"), problem.type());
        assertEquals(detalhe, problem.detail());
    }

    @Test
    void erroDeBancoForaDaClasse23Retorna500() {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new RuntimeException(new SQLException("timeout", "08006"))));

        assertEquals(500, problem.status());
        assertEquals(URI.create("/problems/internal-error"), problem.type());
    }

    @Test
    void erroDeBancoSemSqlStateRetorna500() {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new RuntimeException(new SQLException("erro"))));

        assertEquals(500, problem.status());
    }

    @Test
    void erroInesperadoRetorna500SemExporDetalhesInternos() {
        GlobalExceptionMapper mapper = new GlobalExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new IllegalStateException("NullPointer em X.java:42")));

        assertEquals(500, problem.status());
        assertEquals("Erro interno do servidor", problem.title());
        assertEquals("Ocorreu um erro inesperado ao processar a requisicao.", problem.detail());
    }

    // MismatchedInputExceptionMapper

    private static MismatchedInputException tipoIncompativel(Reference... caminho) {
        MismatchedInputException e = MismatchedInputException.from((JsonParser) null, Integer.class, "tipo errado");
        for (int i = caminho.length - 1; i >= 0; i--) {
            e.prependPath(caminho[i]);
        }
        return e;
    }

    @Test
    void tipoIncompativelIndicaOCampo() {
        MismatchedInputExceptionMapper mapper = new MismatchedInputExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(tipoIncompativel(new Reference(null, "idRegiao"))));

        assertEquals(400, problem.status());
        assertEquals(URI.create("/problems/bad-request"), problem.type());
        assertEquals("Um ou mais campos possuem tipo incompativel.", problem.detail());
        assertEquals(List.of(new Violation("idRegiao", "Valor com tipo incompativel.")), problem.violations());
    }

    @Test
    void tipoIncompativelEmCampoAninhadoEEmLista() {
        MismatchedInputExceptionMapper mapper = new MismatchedInputExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails aninhado = problem(mapper.toResponse(tipoIncompativel(
                new Reference(null, "especificacao"), new Reference(null, "peso"))));
        ProblemDetails lista = problem(mapper.toResponse(tipoIncompativel(
                new Reference(null, "idFornecedores"), new Reference(null, 1))));

        assertEquals("especificacao.peso", aninhado.violations().get(0).field());
        assertEquals("idFornecedores[1]", lista.violations().get(0).field());
    }

    @Test
    void tipoIncompativelSemCampoNaoTemViolacoes() {
        MismatchedInputExceptionMapper mapper = new MismatchedInputExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(tipoIncompativel()));

        assertEquals(400, problem.status());
        assertEquals("O corpo da requisicao nao pode ser interpretado.", problem.detail());
        assertNull(problem.violations());
    }

    // WebApplicationExceptionMapper

    @Test
    void webApplication400ViraBadRequestComDetalhePadrao() {
        WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new WebApplicationException(400)));

        assertEquals(URI.create("/problems/bad-request"), problem.type());
        assertEquals("O corpo da requisicao nao pode ser interpretado.", problem.detail());
    }

    @Test
    void webApplication405UsaAboutBlank() {
        WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(
                new NotAllowedException(Response.status(405).build())));

        assertEquals(405, problem.status());
        assertEquals(URI.create("about:blank"), problem.type());
        assertEquals("Method Not Allowed", problem.title());
        assertNull(problem.detail());
    }

    @Test
    void webApplication415UsaAboutBlank() {
        WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new NotSupportedException()));

        assertEquals(415, problem.status());
        assertEquals("Unsupported Media Type", problem.title());
    }

    @Test
    void webApplicationComMensagemPropriaUsaAMensagem() {
        WebApplicationExceptionMapper mapper = new WebApplicationExceptionMapper();
        mapper.uriInfo = uriInfo;

        ProblemDetails problem = problem(mapper.toResponse(new WebApplicationException("Servico indisponivel", 503)));

        assertEquals(503, problem.status());
        assertEquals("Servico indisponivel", problem.detail());
    }
}
