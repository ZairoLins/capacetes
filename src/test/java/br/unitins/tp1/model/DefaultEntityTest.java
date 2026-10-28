package br.unitins.tp1.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DefaultEntityTest {

    private static Marca marcaComId(Long id) {
        Marca marca = new Marca();
        marca.setId(id);
        return marca;
    }

    @Test
    void entidadesDoMesmoTipoComMesmoIdSaoIguais() {
        Marca a = marcaComId(1L);
        Marca b = marcaComId(1L);
        b.setNome("Outro nome");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void entidadesComIdsDiferentesNaoSaoIguais() {
        assertNotEquals(marcaComId(1L), marcaComId(2L));
    }

    @Test
    void entidadesDeTiposDiferentesComMesmoIdNaoSaoIguais() {
        Cor cor = new Cor();
        cor.setId(1L);

        assertFalse(marcaComId(1L).equals(cor));
    }

    @Test
    void entidadeSemIdSoEIgualAOutraSemId() {
        assertEquals(marcaComId(null), marcaComId(null));
        assertNotEquals(marcaComId(null), marcaComId(1L));
        assertNotEquals(marcaComId(1L), marcaComId(null));
    }

    @Test
    void entidadeNaoEIgualANull() {
        assertFalse(marcaComId(1L).equals(null));
    }

    @Test
    void entidadeEIgualASiMesma() {
        Marca marca = marcaComId(null);
        assertTrue(marca.equals(marca));
    }

    @Test
    void gerarDataCriacaoPreencheADataAtual() {
        Marca marca = new Marca();
        LocalDateTime antes = LocalDateTime.now();

        marca.gerarDataCriacao();

        assertNotNull(marca.getDataCriacao());
        assertFalse(marca.getDataCriacao().isBefore(antes));
        assertNull(marca.getDataAtualizacao());
    }

    @Test
    void gerarDataAtualizacaoPreencheADataAtual() {
        Marca marca = new Marca();
        LocalDateTime antes = LocalDateTime.now();

        marca.gerarDataAtualizacao();

        assertNotNull(marca.getDataAtualizacao());
        assertFalse(marca.getDataAtualizacao().isBefore(antes));
        assertNull(marca.getDataCriacao());
    }
}
