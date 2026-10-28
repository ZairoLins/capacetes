package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.Tamanho;

public record CapaceteOffRoadResponseDTO(
    Long id,
    String modelo,
    BigDecimal preco,
    Tamanho tamanho,
    Integer quantidadeEstoque,
    MarcaResponseDTO marca,
    CategoriaResponseDTO categoria,
    MaterialResponseDTO material,
    CorResponseDTO cor,
    EspecificacaoCapaceteResponseDTO especificacao,
    List<FornecedorResponseDTO> fornecedores,
    Boolean possuiPala
) {
    public static CapaceteOffRoadResponseDTO fromEntity(CapaceteOffRoad capaceteOffRoad) {
        return new CapaceteOffRoadResponseDTO(
            capaceteOffRoad.getId(),
            capaceteOffRoad.getModelo(),
            capaceteOffRoad.getPreco(),
            capaceteOffRoad.getTamanho(),
            capaceteOffRoad.getQuantidadeEstoque(),
            MarcaResponseDTO.fromEntity(capaceteOffRoad.getMarca()),
            CategoriaResponseDTO.fromEntity(capaceteOffRoad.getCategoria()),
            MaterialResponseDTO.fromEntity(capaceteOffRoad.getMaterial()),
            CorResponseDTO.fromEntity(capaceteOffRoad.getCor()),
            EspecificacaoCapaceteResponseDTO.fromEntity(capaceteOffRoad.getEspecificacao()),
            capaceteOffRoad.getFornecedores() == null
                ? List.of()
                : capaceteOffRoad.getFornecedores().stream().map(FornecedorResponseDTO::fromEntity).toList(),
            capaceteOffRoad.getPossuiPala()
        );
    }
}
