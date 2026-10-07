package br.unitins.tp1.dto;

import br.unitins.tp1.model.Material;

public record MaterialResponseDTO(
    Long id, 
    String nome, 
    String descricao
) {

    public static MaterialResponseDTO fromEntity(Material material) {
        return new MaterialResponseDTO(
            material.getId(),
            material.getNome(),
            material.getDescricao()
        );
    }
    
}
