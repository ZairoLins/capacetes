package br.unitins.tp1.exception;

/**
 * Violacao de regra de negocio que depende do estado atual dos dados
 * (registro duplicado, exclusao de registro em uso). Gera resposta 409.
 * Quando a regra se refere a um campo, ele e informado em violations.
 */
public class ConflictException extends RuntimeException {

    private final String field;

    public ConflictException(String message) {
        this(null, message);
    }

    public ConflictException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
