package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.Tamanho;

public record CapaceteIntegralResponseDTO(
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
    Boolean possuiViseiraSolar
) {
    public static CapaceteIntegralResponseDTO fromEntity(CapaceteIntegral capaceteIntegral) {
        return new CapaceteIntegralResponseDTO(
            capaceteIntegral.getId(),
            capaceteIntegral.getModelo(),
            capaceteIntegral.getPreco(),
            capaceteIntegral.getTamanho(),
            capaceteIntegral.getQuantidadeEstoque(),
            MarcaResponseDTO.fromEntity(capaceteIntegral.getMarca()),
            CategoriaResponseDTO.fromEntity(capaceteIntegral.getCategoria()),
            MaterialResponseDTO.fromEntity(capaceteIntegral.getMaterial()),
            CorResponseDTO.fromEntity(capaceteIntegral.getCor()),
            EspecificacaoCapaceteResponseDTO.fromEntity(capaceteIntegral.getEspecificacao()),
            capaceteIntegral.getFornecedores() == null
                ? List.of()
                : capaceteIntegral.getFornecedores().stream().map(FornecedorResponseDTO::fromEntity).toList(),
            capaceteIntegral.getPossuiViseiraSolar()
        );
    }
}
