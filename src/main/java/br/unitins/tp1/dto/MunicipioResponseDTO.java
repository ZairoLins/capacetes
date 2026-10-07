package br.unitins.tp1.dto;

import br.unitins.tp1.model.Estado;
import br.unitins.tp1.model.Municipio;

public record MunicipioResponseDTO(
    Long id,
    String nome,
    Estado estado
) {

    public static MunicipioResponseDTO fromEntity(Municipio municipio) {
        return new MunicipioResponseDTO(
            municipio.getId(),
            municipio.getNome(),
            municipio.getEstado()
        );
    }

}