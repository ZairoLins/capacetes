package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
    @NotBlank(message = "O nome deve ser informado.")
    @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres.")
    String nome,
    @NotBlank(message = "A descricao deve ser informada.")
    @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres.")
    String descricao) {
    
}
