package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EstadoMunicipioDTOTest {

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5 })
    void estadoValidoEmTodasAsRegioes(int idRegiao) {
        assertValido(new EstadoDTO("Tocantins", "TO", idRegiao));
    }

    @Test
    void estadoNomeObrigatorio() {
        assertSomente(new EstadoDTO(null, "TO", 3), "nome", "O nome deve ser informado.");
    }

    @Test
    void estadoNomeComNumeros() {
        assertSomente(new EstadoDTO("T0cantins", "TO", 3), "nome", "O nome deve conter apenas letras.");
    }

    @Test
    void estadoSiglaObrigatoria() {
        assertSomente(new EstadoDTO("Tocantins", null, 3), "sigla", "A sigla deve ser informada.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "to", "T", "TOC", "T1" })
    void estadoSiglaForaDoFormato(String sigla) {
        assertSomente(new EstadoDTO("Tocantins", sigla, 3), "sigla", "A sigla deve conter 2 letras maiusculas.");
    }

    @Test
    void estadoRegiaoObrigatoria() {
        assertSomente(new EstadoDTO("Tocantins", "TO", null), "idRegiao", "A regiao deve ser informada.");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 6 })
    void estadoRegiaoForaDoIntervalo(int idRegiao) {
        assertSomente(new EstadoDTO("Tocantins", "TO", idRegiao), "idRegiao", "Regiao invalida.");
    }

    @Test
    void municipioValido() {
        assertValido(new MunicipioDTO("Palmas", 1L));
    }

    @Test
    void municipioNomeObrigatorio() {
        assertSomente(new MunicipioDTO(null, 1L), "nome", "O nome deve ser informado.");
    }

    @Test
    void municipioNomeComNumeros() {
        assertSomente(new MunicipioDTO("Palmas 2", 1L), "nome", "O nome deve conter apenas letras.");
    }

    @Test
    void municipioEstadoObrigatorio() {
        assertSomente(new MunicipioDTO("Palmas", null), "idEstado", "O estado deve ser informado.");
    }

    @Test
    void municipioEstadoDeveSerPositivo() {
        assertSomente(new MunicipioDTO("Palmas", 0L), "idEstado", "Estado invalido.");
    }
}
