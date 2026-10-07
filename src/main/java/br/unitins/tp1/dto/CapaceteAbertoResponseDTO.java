package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Tamanho;

public record CapaceteAbertoResponseDTO(
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
    Boolean possuiViseira
) {

    public static CapaceteAbertoResponseDTO fromEntity(CapaceteAberto capaceteAberto) {
        return new CapaceteAbertoResponseDTO(
            capaceteAberto.getId(),
            capaceteAberto.getModelo(),
            capaceteAberto.getPreco(),
            capaceteAberto.getTamanho(),
            capaceteAberto.getQuantidadeEstoque(),
            capaceteAberto.getMarca(),
            capaceteAberto.getCategoria(),
            capaceteAberto.getMaterial(),
            capaceteAberto.getCor(),
            capaceteAberto.getEspecificacao(),
            capaceteAberto.getFornecedores(),
            capaceteAberto.getPossuiViseira()
        );
    }

}
