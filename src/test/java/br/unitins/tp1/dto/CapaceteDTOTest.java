package br.unitins.tp1.dto;

import static br.unitins.tp1.dto.Validacao.assertSomente;
import static br.unitins.tp1.dto.Validacao.assertValido;
import static br.unitins.tp1.dto.Validacao.violacoes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Os tres DTOs de capacete compartilham as mesmas regras; cada caso e
 * verificado nos tres. Muda apenas o campo booleano especifico de cada tipo.
 */
class CapaceteDTOTest {

    /** Dados validos de um capacete; cada teste altera so o que interessa */
    private static final class Dados {
        String modelo = "Jet Classic";
        BigDecimal preco = new BigDecimal("899.90");
        Integer idTamanho = 3;
        Integer quantidadeEstoque = 10;
        Long idMarca = 1L;
        Long idCategoria = 1L;
        Long idMaterial = 1L;
        Long idCor = 1L;
        EspecificacaoCapaceteDTO especificacao = new EspecificacaoCapaceteDTO(new BigDecimal("1.350"), 2);
        List<Long> idFornecedores = List.of(1L, 2L);
        Boolean booleano = true;

        List<Object> dtos() {
            return List.of(
                    new CapaceteAbertoDTO(modelo, preco, idTamanho, quantidadeEstoque, idMarca, idCategoria,
                            idMaterial, idCor, especificacao, idFornecedores, booleano),
                    new CapaceteIntegralDTO(modelo, preco, idTamanho, quantidadeEstoque, idMarca, idCategoria,
                            idMaterial, idCor, especificacao, idFornecedores, booleano),
                    new CapaceteOffRoadDTO(modelo, preco, idTamanho, quantidadeEstoque, idMarca, idCategoria,
                            idMaterial, idCor, especificacao, idFornecedores, booleano));
        }
    }

    private static List<Object> dtos(Consumer<Dados> alteracao) {
        Dados dados = new Dados();
        alteracao.accept(dados);
        return dados.dtos();
    }

    private static void assertSomenteEmTodos(Consumer<Dados> alteracao, String campo, String... mensagens) {
        for (Object dto : dtos(alteracao)) {
            assertSomente(dto, campo, mensagens);
        }
    }

    @Test
    void dadosValidos() {
        dtos(d -> { }).forEach(Validacao::assertValido);
    }

    @Test
    void fornecedoresSaoOpcionais() {
        dtos(d -> d.idFornecedores = null).forEach(Validacao::assertValido);
        dtos(d -> d.idFornecedores = List.of()).forEach(Validacao::assertValido);
    }

    @Test
    void estoqueZeroEPermitido() {
        dtos(d -> d.quantidadeEstoque = 0).forEach(Validacao::assertValido);
    }

    @Test
    void modeloObrigatorio() {
        assertSomenteEmTodos(d -> d.modelo = null, "modelo", "O modelo deve ser informado.");
    }

    @Test
    void modeloCurto() {
        assertSomenteEmTodos(d -> d.modelo = "J", "modelo", "O modelo deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void modeloLongo() {
        assertSomenteEmTodos(d -> d.modelo = "J".repeat(61), "modelo", "O modelo deve ter entre 2 e 60 caracteres.");
    }

    @Test
    void precoObrigatorio() {
        assertSomenteEmTodos(d -> d.preco = null, "preco", "O preco deve ser informado.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "-10.00" })
    void precoDeveSerPositivo(String preco) {
        assertSomenteEmTodos(d -> d.preco = new BigDecimal(preco), "preco", "O preco deve ser maior que zero.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "10.999", "123456789.00" })
    void precoComDigitosDemais(String preco) {
        assertSomenteEmTodos(d -> d.preco = new BigDecimal(preco), "preco",
                "O preco deve ter no maximo 8 digitos inteiros e 2 casas decimais.");
    }

    @Test
    void tamanhoObrigatorio() {
        assertSomenteEmTodos(d -> d.idTamanho = null, "idTamanho", "O tamanho deve ser informado.");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 7 })
    void tamanhoForaDoIntervalo(int idTamanho) {
        assertSomenteEmTodos(d -> d.idTamanho = idTamanho, "idTamanho", "Tamanho invalido.");
    }

    @Test
    void estoqueObrigatorio() {
        assertSomenteEmTodos(d -> d.quantidadeEstoque = null, "quantidadeEstoque",
                "A quantidade em estoque deve ser informada.");
    }

    @Test
    void estoqueNegativo() {
        assertSomenteEmTodos(d -> d.quantidadeEstoque = -1, "quantidadeEstoque",
                "A quantidade em estoque nao pode ser negativa.");
    }

    @Test
    void referenciasObrigatorias() {
        assertSomenteEmTodos(d -> d.idMarca = null, "idMarca", "A marca deve ser informada.");
        assertSomenteEmTodos(d -> d.idCategoria = null, "idCategoria", "A categoria deve ser informada.");
        assertSomenteEmTodos(d -> d.idMaterial = null, "idMaterial", "O material deve ser informado.");
        assertSomenteEmTodos(d -> d.idCor = null, "idCor", "A cor deve ser informada.");
    }

    @Test
    void referenciasDevemSerPositivas() {
        assertSomenteEmTodos(d -> d.idMarca = 0L, "idMarca", "Marca invalida.");
        assertSomenteEmTodos(d -> d.idCategoria = -1L, "idCategoria", "Categoria invalida.");
        assertSomenteEmTodos(d -> d.idMaterial = 0L, "idMaterial", "Material invalido.");
        assertSomenteEmTodos(d -> d.idCor = 0L, "idCor", "Cor invalida.");
    }

    @Test
    void especificacaoObrigatoria() {
        assertSomenteEmTodos(d -> d.especificacao = null, "especificacao", "A especificacao deve ser informada.");
    }

    @Test
    void especificacaoEValidadaEmCascata() {
        assertSomenteEmTodos(d -> d.especificacao = new EspecificacaoCapaceteDTO(new BigDecimal("0.4"), 2),
                "especificacao.peso", "O peso deve ser de no minimo 0,5 kg.");
    }

    @Test
    void fornecedorNuloOuNaoPositivoNaLista() {
        for (Object dto : dtos(d -> d.idFornecedores = Arrays.asList(1L, null, -5L))) {
            assertEquals(Map.of(
                    "idFornecedores[1].<list element>", Set.of("O fornecedor deve ser informado."),
                    "idFornecedores[2].<list element>", Set.of("Fornecedor invalido.")), violacoes(dto));
        }
    }

    @Test
    void campoBooleanoEspecificoDeCadaTipoEObrigatorio() {
        List<Object> dtos = dtos(d -> d.booleano = null);

        assertSomente(dtos.get(0), "possuiViseira", "Deve ser informado se possui viseira.");
        assertSomente(dtos.get(1), "possuiViseiraSolar", "Deve ser informado se possui viseira solar.");
        assertSomente(dtos.get(2), "possuiPala", "Deve ser informado se possui pala.");
    }

    @ParameterizedTest
    @ValueSource(strings = { "0.5", "1.234", "3.0", "3" })
    void especificacaoPesoNosLimites(String peso) {
        assertValido(new EspecificacaoCapaceteDTO(new BigDecimal(peso), 1));
    }

    @Test
    void especificacaoPesoObrigatorio() {
        assertSomente(new EspecificacaoCapaceteDTO(null, 1), "peso", "O peso deve ser informado.");
    }

    @Test
    void especificacaoPesoAcimaDoMaximo() {
        assertSomente(new EspecificacaoCapaceteDTO(new BigDecimal("3.001"), 1), "peso",
                "O peso deve ser de no maximo 3 kg.");
    }

    @Test
    void especificacaoPesoComMaisDe3CasasDecimais() {
        assertSomente(new EspecificacaoCapaceteDTO(new BigDecimal("1.2345"), 1), "peso",
                "O peso deve ter no maximo 3 casas decimais.");
    }

    @Test
    void especificacaoTipoFechamentoObrigatorio() {
        assertSomente(new EspecificacaoCapaceteDTO(new BigDecimal("1.2"), null), "idTipoFechamento",
                "O tipo de fechamento deve ser informado.");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 4 })
    void especificacaoTipoFechamentoForaDoIntervalo(int idTipoFechamento) {
        assertSomente(new EspecificacaoCapaceteDTO(new BigDecimal("1.2"), idTipoFechamento), "idTipoFechamento",
                "Tipo de fechamento invalido.");
    }
}
