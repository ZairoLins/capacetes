package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcaDTO(
    @NotBlank(message = "O nome deve ser informado.")
    @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres.")
    String nome,
    @NotBlank(message = "O pais de origem deve ser informado.")
    @Size(min = 2, max = 60, message = "O pais de origem deve ter entre 2 e 60 caracteres.")
    String paisOrigem) {
    
}
