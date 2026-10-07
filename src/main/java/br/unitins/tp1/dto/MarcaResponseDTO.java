package br.unitins.tp1.dto;

import br.unitins.tp1.model.Marca;

public record MarcaResponseDTO(
    Long id, 
    String nome, 
    String paisOrigem
) {

    public static MarcaResponseDTO fromEntity(Marca marca) {
        return new MarcaResponseDTO(
            marca.getId(),
            marca.getNome(),
            marca.getPaisOrigem()
        );
    }
    
}
