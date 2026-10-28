package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;

import org.junit.jupiter.api.Test;

/**
 * CategoriaDTO e MaterialDTO tem as mesmas regras (nome + descricao).
 */
class CategoriaMaterialDTOTest {

    @Test
    void categoriaValida() {
        assertValido(new CategoriaDTO("Urbano", "Uso na cidade"));
    }

    @Test
    void categoriaNomeObrigatorio() {
        assertSomente(new CategoriaDTO(null, "Uso na cidade"), "nome", "O nome deve ser informado.");
    }

    @Test
    void categoriaNomeComNumeros() {
        assertSomente(new CategoriaDTO("Urbano 2", "Uso na cidade"), "nome", "O nome deve conter apenas letras.");
    }

    @Test
    void categoriaNomeCurto() {
        assertSomente(new CategoriaDTO("U", "Uso na cidade"), "nome", "O nome deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void categoriaDescricaoObrigatoria() {
        assertSomente(new CategoriaDTO("Urbano", null), "descricao", "A descricao deve ser informada.");
    }

    @Test
    void categoriaDescricaoCurta() {
        assertSomente(new CategoriaDTO("Urbano", "Uso"), "descricao", "A descricao deve ter entre 5 e 255 caracteres.");
    }

    @Test
    void categoriaDescricaoLonga() {
        assertSomente(new CategoriaDTO("Urbano", "a".repeat(256)), "descricao",
                "A descricao deve ter entre 5 e 255 caracteres.");
    }

    @Test
    void categoriaDescricaoNosLimites() {
        assertValido(new CategoriaDTO("Urbano", "a".repeat(5)));
        assertValido(new CategoriaDTO("Urbano", "a".repeat(255)));
    }

    @Test
    void materialValido() {
        assertValido(new MaterialDTO("Fibra de carbono", "Material leve e resistente"));
    }

    @Test
    void materialNomeObrigatorio() {
        assertSomente(new MaterialDTO(null, "Material leve"), "nome", "O nome deve ser informado.");
    }

    @Test
    void materialNomeComNumeros() {
        assertSomente(new MaterialDTO("ABS 3", "Material leve"), "nome", "O nome deve conter apenas letras.");
    }

    @Test
    void materialDescricaoObrigatoria() {
        assertSomente(new MaterialDTO("Policarbonato", " "), "descricao",
                "A descricao deve ser informada.", "A descricao deve ter entre 5 e 255 caracteres.");
    }

    @Test
    void materialDescricaoCurta() {
        assertSomente(new MaterialDTO("Policarbonato", "Leve"), "descricao",
                "A descricao deve ter entre 5 e 255 caracteres.");
    }
}
