package br.unitins.tp1.exception;

/**
 * Violacao de regra de negocio sobre um dado enviado pelo cliente: a
 * requisicao esta bem formada, mas o valor nao faz sentido no dominio
 * (ex.: estoque insuficiente para a quantidade solicitada).
 * Gera resposta 422 com o campo em violations.
 */
public class BusinessValidationException extends RuntimeException {

    private final String field;

    public BusinessValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
