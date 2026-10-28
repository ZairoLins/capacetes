package br.unitins.tp1.model.converterjpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.unitins.tp1.model.Regiao;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import jakarta.ws.rs.BadRequestException;

/**
 * Os converters gravam o id do enum no banco e o leem de volta. Um id invalido
 * vindo do banco e erro do servidor (IllegalStateException), nao 400.
 */
class ConvertersTest {

    private final RegiaoConverter regiaoConverter = new RegiaoConverter();
    private final TamanhoConverter tamanhoConverter = new TamanhoConverter();
    private final TipoFechamentoConverter tipoFechamentoConverter = new TipoFechamentoConverter();

    @ParameterizedTest
    @EnumSource(Regiao.class)
    void regiaoConverteIdaEVolta(Regiao regiao) {
        Integer coluna = regiaoConverter.convertToDatabaseColumn(regiao);

        assertEquals(regiao.getId(), coluna);
        assertSame(regiao, regiaoConverter.convertToEntityAttribute(coluna));
    }

    @Test
    void regiaoConverteNullEmNull() {
        assertNull(regiaoConverter.convertToDatabaseColumn(null));
        assertNull(regiaoConverter.convertToEntityAttribute(null));
    }

    @Test
    void regiaoComIdInvalidoNoBancoLancaIllegalState() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> regiaoConverter.convertToEntityAttribute(99));

        assertEquals("Valor invalido no banco para Regiao: 99", e.getMessage());
        assertInstanceOf(BadRequestException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(Tamanho.class)
    void tamanhoConverteIdaEVolta(Tamanho tamanho) {
        Integer coluna = tamanhoConverter.convertToDatabaseColumn(tamanho);

        assertEquals(tamanho.getId(), coluna);
        assertSame(tamanho, tamanhoConverter.convertToEntityAttribute(coluna));
    }

    @Test
    void tamanhoConverteNullEmNull() {
        assertNull(tamanhoConverter.convertToDatabaseColumn(null));
        assertNull(tamanhoConverter.convertToEntityAttribute(null));
    }

    @Test
    void tamanhoComIdInvalidoNoBancoLancaIllegalState() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> tamanhoConverter.convertToEntityAttribute(99));

        assertEquals("Valor invalido no banco para Tamanho: 99", e.getMessage());
        assertInstanceOf(BadRequestException.class, e.getCause());
    }

    @ParameterizedTest
    @EnumSource(TipoFechamento.class)
    void tipoFechamentoConverteIdaEVolta(TipoFechamento tipoFechamento) {
        Integer coluna = tipoFechamentoConverter.convertToDatabaseColumn(tipoFechamento);

        assertEquals(tipoFechamento.getId(), coluna);
        assertSame(tipoFechamento, tipoFechamentoConverter.convertToEntityAttribute(coluna));
    }

    @Test
    void tipoFechamentoConverteNullEmNull() {
        assertNull(tipoFechamentoConverter.convertToDatabaseColumn(null));
        assertNull(tipoFechamentoConverter.convertToEntityAttribute(null));
    }

    @Test
    void tipoFechamentoComIdInvalidoNoBancoLancaIllegalState() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> tipoFechamentoConverter.convertToEntityAttribute(99));

        assertEquals("Valor invalido no banco para TipoFechamento: 99", e.getMessage());
        assertInstanceOf(BadRequestException.class, e.getCause());
    }
}
