package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FornecedorDTOTest {

    private static final String CNPJ_VALIDO = "11222333000181";
    private static final String CELULAR_VALIDO = "63992345678";
    private static final String TELEFONE_INVALIDO =
            "Telefone invalido: use um DDD valido e um numero fixo (8 digitos) ou celular (9 digitos iniciando com 9).";

    @ParameterizedTest
    @ValueSource(strings = { "63992345678", "6332145678", "11987654321", "1155554444" })
    void aceitaTelefoneFixoOuCelularValido(String telefone) {
        assertValido(new FornecedorDTO("Moto Pecas LTDA", CNPJ_VALIDO, telefone));
    }

    @ParameterizedTest
    @ValueSource(strings = { "11222333000181", "11444777000161" })
    void aceitaCnpjValido(String cnpj) {
        assertValido(new FornecedorDTO("Moto Pecas LTDA", cnpj, CELULAR_VALIDO));
    }

    @Test
    void razaoSocialObrigatoria() {
        assertSomente(new FornecedorDTO(null, CNPJ_VALIDO, CELULAR_VALIDO), "razaoSocial",
                "A razao social deve ser informada.");
    }

    @Test
    void razaoSocialLonga() {
        assertSomente(new FornecedorDTO("A".repeat(101), CNPJ_VALIDO, CELULAR_VALIDO), "razaoSocial",
                "A razao social deve ter entre 2 e 100 caracteres.");
    }

    @Test
    void razaoSocialAceitaNumerosESimbolos() {
        assertValido(new FornecedorDTO("Moto & Cia 2 LTDA.", CNPJ_VALIDO, CELULAR_VALIDO));
    }

    @Test
    void cnpjObrigatorio() {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", null, CELULAR_VALIDO), "cnpj", "O CNPJ deve ser informado.");
    }

    @Test
    void cnpjComDigitoVerificadorErrado() {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", "11222333000182", CELULAR_VALIDO), "cnpj", "CNPJ invalido.");
    }

    @Test
    void cnpjComTodosOsDigitosIguais() {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", "00000000000000", CELULAR_VALIDO), "cnpj", "CNPJ invalido.");
    }

    @Test
    void cnpjComPontuacaoNaoEAceito() {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", "11.222.333/0001-81", CELULAR_VALIDO), "cnpj",
                "O CNPJ deve conter 14 digitos numericos.");
    }

    @Test
    void telefoneObrigatorio() {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", CNPJ_VALIDO, null), "telefone",
                "O telefone deve ser informado.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "123", "(63) 99234-5678", "639923456789" })
    void telefoneForaDoFormatoMostraSoAMensagemDeFormato(String telefone) {
        // o segundo @Pattern so avalia numeros com 10 ou 11 digitos, para nao repetir o erro
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", CNPJ_VALIDO, telefone), "telefone",
                "O telefone deve conter DDD e numero, apenas digitos.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "0332145678",  // DDD com zero
        "6312345678",  // fixo iniciando com 1
        "6362345678",  // fixo iniciando com 6
        "63812345678", // celular sem o 9 inicial
    })
    void telefoneComDddOuNumeroInvalido(String telefone) {
        assertSomente(new FornecedorDTO("Moto Pecas LTDA", CNPJ_VALIDO, telefone), "telefone", TELEFONE_INVALIDO);
    }
}
