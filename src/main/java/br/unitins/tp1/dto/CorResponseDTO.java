package br.unitins.tp1.dto;

import br.unitins.tp1.model.Cor;

public record CorResponseDTO(
    Long id, 
    String nome, 
    String codigoHexadecimal
) {

    public static CorResponseDTO fromEntity(Cor cor) {
        return new CorResponseDTO(
            cor.getId(),
            cor.getNome(),
            cor.getCodigoHexadecimal()
        );
    }
    
}
