package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Tamanho;

public record CapaceteOffRoadResponseDTO(
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
    Boolean possuiPala
) {

    public static CapaceteOffRoadResponseDTO fromEntity(CapaceteOffRoad capaceteOffRoad) {
        return new CapaceteOffRoadResponseDTO(
            capaceteOffRoad.getId(),
            capaceteOffRoad.getModelo(),
            capaceteOffRoad.getPreco(),
            capaceteOffRoad.getTamanho(),
            capaceteOffRoad.getQuantidadeEstoque(),
            capaceteOffRoad.getMarca(),
            capaceteOffRoad.getCategoria(),
            capaceteOffRoad.getMaterial(),
            capaceteOffRoad.getCor(),
            capaceteOffRoad.getEspecificacao(),
            capaceteOffRoad.getFornecedores(),
            capaceteOffRoad.getPossuiPala()
        );
    }

}
