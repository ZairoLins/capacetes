package br.unitins.tp1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.model.Capacete;
import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Estado;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Municipio;
import br.unitins.tp1.model.Regiao;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;

/**
 * Entidades prontas para os testes unitarios, ja com id preenchido.
 */

public final class Fixtures {

    private Fixtures() {
    }

    public static Cor cor(Long id, String nome, String codigoHexadecimal) {
        Cor cor = new Cor();
        cor.setId(id);
        cor.setNome(nome);
        cor.setCodigoHexadecimal(codigoHexadecimal);
        return cor;
    }

    public static Categoria categoria(Long id, String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }

    public static Marca marca(Long id, String nome, String paisOrigem) {
        Marca marca = new Marca();
        marca.setId(id);
        marca.setNome(nome);
        marca.setPaisOrigem(paisOrigem);
        return marca;
    }

    public static Material material(Long id, String nome, String descricao) {
        Material material = new Material();
        material.setId(id);
        material.setNome(nome);
        material.setDescricao(descricao);
        return material;
    }

    public static Estado estado(Long id, String nome, String sigla, Regiao regiao) {
        Estado estado = new Estado();
        estado.setId(id);
        estado.setNome(nome);
        estado.setSigla(sigla);
        estado.setRegiao(regiao);
        return estado;
    }

    public static Municipio municipio(Long id, String nome, Estado estado) {
        Municipio municipio = new Municipio();
        municipio.setId(id);
        municipio.setNome(nome);
        municipio.setEstado(estado);
        return municipio;
    }

    public static Fornecedor fornecedor(Long id, String razaoSocial, String cnpj, String telefone) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setRazaoSocial(razaoSocial);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone(telefone);
        return fornecedor;
    }

    public static EspecificacaoCapacete especificacao(String peso, TipoFechamento tipoFechamento) {
        EspecificacaoCapacete especificacao = new EspecificacaoCapacete();
        especificacao.setPeso(new BigDecimal(peso));
        especificacao.setTipoFechamento(tipoFechamento);
        return especificacao;
    }

    public static CapaceteAberto capaceteAberto(Long id, String modelo) {
        CapaceteAberto capacete = preencher(new CapaceteAberto(), id, modelo);
        capacete.setPossuiViseira(true);
        return capacete;
    }

    public static CapaceteIntegral capaceteIntegral(Long id, String modelo) {
        CapaceteIntegral capacete = preencher(new CapaceteIntegral(), id, modelo);
        capacete.setPossuiViseiraSolar(true);
        return capacete;
    }

    public static CapaceteOffRoad capaceteOffRoad(Long id, String modelo) {
        CapaceteOffRoad capacete = preencher(new CapaceteOffRoad(), id, modelo);
        capacete.setPossuiPala(true);
        return capacete;
    }

    private static <T extends Capacete> T preencher(T capacete, Long id, String modelo) {
        capacete.setId(id);
        capacete.setModelo(modelo);
        capacete.setPreco(new BigDecimal("899.90"));
        capacete.setTamanho(Tamanho.M);
        capacete.setQuantidadeEstoque(10);
        capacete.setMarca(marca(1L, "LS2", "Espanha"));
        capacete.setCategoria(categoria(1L, "Urbano", "Uso na cidade"));
        capacete.setMaterial(material(1L, "Policarbonato", "Material leve"));
        capacete.setCor(cor(1L, "Preto", "#000000"));
        capacete.setEspecificacao(especificacao("1.350", TipoFechamento.MICROMETRICO));
        capacete.setFornecedores(new ArrayList<>(List.of(
                fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678"))));
        return capacete;
    }
}
