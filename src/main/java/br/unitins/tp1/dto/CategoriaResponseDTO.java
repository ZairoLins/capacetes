package br.unitins.tp1.dto;

import br.unitins.tp1.model.Categoria;

public record CategoriaResponseDTO(
    Long id, 
    String nome, 
    String descricao
) {

    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        return new CategoriaResponseDTO(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao()
        );
    }
    
}
