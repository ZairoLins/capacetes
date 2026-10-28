package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;

import org.junit.jupiter.api.Test;

class MarcaDTOTest {

    @Test
    void marcaValida() {
        assertValido(new MarcaDTO("LS2", "Espanha"));
    }

    @Test
    void nomeAceitaNumeros() {
        // diferente das outras entidades, o nome da marca nao tem restricao de caracteres
        assertValido(new MarcaDTO("X-11 Racing 2", "Brasil"));
    }

    @Test
    void nomeObrigatorio() {
        assertSomente(new MarcaDTO(null, "Espanha"), "nome", "O nome deve ser informado.");
    }

    @Test
    void nomeCurto() {
        assertSomente(new MarcaDTO("X", "Espanha"), "nome", "O nome deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void nomeLongo() {
        assertSomente(new MarcaDTO("X".repeat(61), "Espanha"), "nome", "O nome deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void paisOrigemObrigatorio() {
        assertSomente(new MarcaDTO("LS2", null), "paisOrigem", "O pais de origem deve ser informado.");
    }

    @Test
    void paisOrigemComNumeros() {
        assertSomente(new MarcaDTO("LS2", "Espanha1"), "paisOrigem", "O pais de origem deve conter apenas letras.");
    }

    @Test
    void paisOrigemCurto() {
        assertSomente(new MarcaDTO("LS2", "E"), "paisOrigem", "O pais de origem deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void paisOrigemComAcentosEEspacos() {
        assertValido(new MarcaDTO("Shoei", "Japão"));
        assertValido(new MarcaDTO("Bell", "Estados Unidos"));
    }
}
