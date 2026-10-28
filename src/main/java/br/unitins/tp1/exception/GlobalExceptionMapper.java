package br.unitins.tp1.exception;

import java.sql.SQLException;

import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Ultimo recurso: trata qualquer excecao que nao tenha um mapper mais
 * especifico, para que nenhuma resposta saia fora do padrao Problem Details
 * nem exponha stack trace, SQL ou nomes de tabelas ao cliente.
 *
 * Violacoes de integridade do banco (ex.: excluir uma marca usada por um
 * capacete) viram 409; o restante vira 500 e e registrado no log.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    // codigos SQLState padrao (classe 23 = violacao de restricao de integridade)
    private static final String CHAVE_ESTRANGEIRA = "23503";
    private static final String VALOR_DUPLICADO = "23505";

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        SQLException erroBanco = causaSql(exception);
        if (erroBanco != null && erroBanco.getSQLState() != null && erroBanco.getSQLState().startsWith("23")) {
            return ProblemResponses.build(ProblemType.CONFLICT, detalheIntegridade(erroBanco.getSQLState()), uriInfo);
        }

        LOG.errorf(exception, "Erro nao tratado em %s", uriInfo.getPath());
        return ProblemResponses.build(ProblemType.INTERNAL_ERROR,
                "Ocorreu um erro inesperado ao processar a requisicao.", uriInfo);
    }

    // o erro do banco chega embrulhado (transacao, Hibernate...), por isso percorre as causas
    private SQLException causaSql(Throwable exception) {
        for (Throwable causa = exception; causa != null; causa = causa.getCause()) {
            if (causa instanceof SQLException sql) {
                return sql;
            }
        }
        return null;
    }

    private String detalheIntegridade(String sqlState) {
        return switch (sqlState) {
            case CHAVE_ESTRANGEIRA -> "O registro esta vinculado a outros registros e a operacao nao pode ser concluida.";
            case VALOR_DUPLICADO -> "Ja existe um registro com os dados informados.";
            default -> "A operacao viola uma restricao de integridade dos dados.";
        };
    }
}
