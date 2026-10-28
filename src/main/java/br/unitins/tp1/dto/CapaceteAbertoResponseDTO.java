package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.Tamanho;

public record CapaceteAbertoResponseDTO(
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
    Boolean possuiViseira
) {
    public static CapaceteAbertoResponseDTO fromEntity(CapaceteAberto capaceteAberto) {
        return new CapaceteAbertoResponseDTO(
            capaceteAberto.getId(),
            capaceteAberto.getModelo(),
            capaceteAberto.getPreco(),
            capaceteAberto.getTamanho(),
            capaceteAberto.getQuantidadeEstoque(),
            MarcaResponseDTO.fromEntity(capaceteAberto.getMarca()),
            CategoriaResponseDTO.fromEntity(capaceteAberto.getCategoria()),
            MaterialResponseDTO.fromEntity(capaceteAberto.getMaterial()),
            CorResponseDTO.fromEntity(capaceteAberto.getCor()),
            EspecificacaoCapaceteResponseDTO.fromEntity(capaceteAberto.getEspecificacao()),
            capaceteAberto.getFornecedores() == null
                ? List.of()
                : capaceteAberto.getFornecedores().stream().map(FornecedorResponseDTO::fromEntity).toList(),
            capaceteAberto.getPossuiViseira()
        );
    }
}
