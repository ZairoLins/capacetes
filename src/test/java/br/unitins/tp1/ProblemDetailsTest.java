package br.unitins.tp1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * Verifica que os erros seguem o formato Problem Details (RFC 9457).
 * Os casos nao dependem dos dados do banco: falham antes de qualquer consulta.
 */
@QuarkusTest
class ProblemDetailsTest {

    private static final String PROBLEM_JSON = "application/problem+json";

    @Test
    void recursoInexistenteRetorna404() {
        given()
            .when().get("/rota-inexistente")
            .then()
                .statusCode(404)
                .contentType(PROBLEM_JSON)
                .body("type", is("/problems/not-found"))
                .body("title", is("Recurso nao encontrado"))
                .body("status", is(404))
                .body("instance", is("/rota-inexistente"));
    }

    @Test
    void dadosInvalidosRetornam400ComViolacoesPorCampo() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"A\",\"codigoHexadecimal\":\"FFF\"}")
            .when().post("/cores")
            .then()
                .statusCode(400)
                .contentType(PROBLEM_JSON)
                .body("type", is("/problems/validation-error"))
                .body("status", is(400))
                .body("instance", is("/cores"))
                .body("violations.field", hasItems("nome", "codigoHexadecimal"))
                .body("violations.message", hasItems(
                        "O nome deve ter entre 2 e 60 caracteres.",
                        "O codigo hexadecimal deve estar no formato #RRGGBB."));
    }

    @Test
    void campoAninhadoInvalidoApareceComCaminhoCompleto() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"modelo\":\"Teste\",\"especificacao\":{\"peso\":9}}")
            .when().post("/capacetes-abertos")
            .then()
                .statusCode(400)
                .body("violations.field", hasItems("especificacao.peso", "idMarca"));
    }

    @Test
    void jsonMalformadoRetorna400() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":")
            .when().post("/cores")
            .then()
                .statusCode(400)
                .contentType(PROBLEM_JSON)
                .body("type", is("/problems/bad-request"));
    }

    @Test
    void tipoIncompativelIndicaOCampo() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"nome\":\"Bahia\",\"sigla\":\"BA\",\"idRegiao\":\"abc\"}")
            .when().post("/estados")
            .then()
                .statusCode(400)
                .body("type", is("/problems/bad-request"))
                .body("violations.field", hasItems("idRegiao"));
    }

    @Test
    void buscaSemParametroRetorna400() {
        given()
            .when().get("/marcas/search")
            .then()
                .statusCode(400)
                .body("violations.field", hasItems("nome"));
    }

    @Test
    void metodoNaoPermitidoRetorna405() {
        given()
            .when().patch("/cores/1")
            .then()
                .statusCode(405)
                .contentType(PROBLEM_JSON)
                .body("type", is("about:blank"))
                .body("title", is("Method Not Allowed"));
    }
}
