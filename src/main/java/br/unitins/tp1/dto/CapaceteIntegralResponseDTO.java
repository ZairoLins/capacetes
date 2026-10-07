package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Tamanho;

public record CapaceteIntegralResponseDTO(
    Long id,
    String modelo,
    BigDecimal preco,
    Tamanho tamanho,
    Integer quantidadeEstoque,
    Marca marca,
    Categoria categoria,
    Material material,
    Cor cor,
    EspecificacaoCapacete especificacao,
    List<Fornecedor> fornecedores,
    Boolean possuiViseiraSolar
) {

    public static CapaceteIntegralResponseDTO fromEntity(CapaceteIntegral capaceteIntegral) {
        return new CapaceteIntegralResponseDTO(
            capaceteIntegral.getId(),
            capaceteIntegral.getModelo(),
            capaceteIntegral.getPreco(),
            capaceteIntegral.getTamanho(),
            capaceteIntegral.getQuantidadeEstoque(),
            capaceteIntegral.getMarca(),
            capaceteIntegral.getCategoria(),
            capaceteIntegral.getMaterial(),
            capaceteIntegral.getCor(),
            capaceteIntegral.getEspecificacao(),
            capaceteIntegral.getFornecedores(),
            capaceteIntegral.getPossuiViseiraSolar()
        );
    }

}
