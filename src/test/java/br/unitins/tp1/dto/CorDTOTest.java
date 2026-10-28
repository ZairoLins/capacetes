package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CorDTOTest {

    @ParameterizedTest
    @ValueSource(strings = { "#000000", "#ffffff", "#A1b2C3" })
    void aceitaCodigoHexadecimalValido(String codigo) {
        assertValido(new CorDTO("Azul Royal", codigo));
    }

    @Test
    void aceitaNomeComAcentosEspacosEHifen() {
        assertValido(new CorDTO("Verde-limão d'água", "#00FF00"));
    }

    @Test
    void nomeEObrigatorio() {
        assertSomente(new CorDTO(null, "#000000"), "nome", "O nome deve ser informado.");
    }

    @Test
    void nomeEmBrancoViolaObrigatoriedadeETamanho() {
        assertSomente(new CorDTO(" ", "#000000"), "nome",
                "O nome deve ser informado.", "O nome deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void nomeComMaisDe60Caracteres() {
        assertSomente(new CorDTO("A".repeat(61), "#000000"), "nome", "O nome deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void nomeComNumeros() {
        assertSomente(new CorDTO("Preto 2", "#000000"), "nome", "O nome deve conter apenas letras.");
    }

    @Test
    void codigoHexadecimalEObrigatorio() {
        assertSomente(new CorDTO("Preto", null), "codigoHexadecimal", "O codigo hexadecimal deve ser informado.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "000000", "#FFF", "#GGGGGG", "#0000000" })
    void codigoHexadecimalForaDoFormato(String codigo) {
        assertSomente(new CorDTO("Preto", codigo), "codigoHexadecimal",
                "O codigo hexadecimal deve estar no formato #RRGGBB.");
    }
}
