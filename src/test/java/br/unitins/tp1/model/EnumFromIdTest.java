package br.unitins.tp1.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.ws.rs.BadRequestException;

/**
 * fromId deve encontrar cada constante pelo seu id e rejeitar ids desconhecidos
 * com 400 (o id vem da requisicao).
 */
class EnumFromIdTest {

    @ParameterizedTest
    @EnumSource(Regiao.class)
    void regiaoFromIdRetornaAConstanteDoId(Regiao regiao) {
        assertSame(regiao, Regiao.fromId(regiao.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 6, -1 })
    void regiaoFromIdRejeitaIdInexistente(int id) {
        BadRequestException e = assertThrows(BadRequestException.class, () -> Regiao.fromId(id));
        assertEquals("Regiao invalida: " + id, e.getMessage());
    }

    @Test
    void regiaoTemIdsENomesEsperados() {
        assertEquals(1, Regiao.CENTRO_OESTE.getId());
        assertEquals("Centro-Oeste", Regiao.CENTRO_OESTE.getNome());
        assertEquals(5, Regiao.SUL.getId());
        assertEquals("Sul", Regiao.SUL.getNome());
    }

    @ParameterizedTest
    @EnumSource(Tamanho.class)
    void tamanhoFromIdRetornaAConstanteDoId(Tamanho tamanho) {
        assertSame(tamanho, Tamanho.fromId(tamanho.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 7, -1 })
    void tamanhoFromIdRejeitaIdInexistente(int id) {
        BadRequestException e = assertThrows(BadRequestException.class, () -> Tamanho.fromId(id));
        assertEquals("Tamanho invalido: " + id, e.getMessage());
    }

    @Test
    void tamanhoTemIdsENomesEsperados() {
        assertEquals(1, Tamanho.XS.getId());
        assertEquals("XS", Tamanho.XS.getNome());
        assertEquals(6, Tamanho.XXL.getId());
        assertEquals("XXL", Tamanho.XXL.getNome());
    }

    @ParameterizedTest
    @EnumSource(TipoFechamento.class)
    void tipoFechamentoFromIdRetornaAConstanteDoId(TipoFechamento tipoFechamento) {
        assertSame(tipoFechamento, TipoFechamento.fromId(tipoFechamento.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 4, -1 })
    void tipoFechamentoFromIdRejeitaIdInexistente(int id) {
        BadRequestException e = assertThrows(BadRequestException.class, () -> TipoFechamento.fromId(id));
        assertEquals("Tipo de fechamento invalido: " + id, e.getMessage());
    }

    @Test
    void tipoFechamentoTemIdsENomesEsperados() {
        assertEquals(1, TipoFechamento.DUPLO_D.getId());
        assertEquals("Duplo D", TipoFechamento.DUPLO_D.getNome());
        assertEquals(3, TipoFechamento.ENGATE_RAPIDO.getId());
        assertEquals("Engate Rapido", TipoFechamento.ENGATE_RAPIDO.getNome());
    }
}
