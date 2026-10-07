package br.unitins.tp1.dto;

import br.unitins.tp1.model.Fornecedor;

public record FornecedorResponseDTO(
    Long id,
    String razaoSocial,
    String cnpj,
    String telefone
) {

    public static FornecedorResponseDTO fromEntity(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
            fornecedor.getId(),
            fornecedor.getRazaoSocial(),
            fornecedor.getCnpj(),
            fornecedor.getTelefone()
        );
    }

}
