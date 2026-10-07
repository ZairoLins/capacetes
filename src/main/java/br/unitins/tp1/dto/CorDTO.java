package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CorDTO(
    @NotBlank(message = "O nome deve ser informado.")
    @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres.")
    String nome,
    @NotBlank(message = "O codigo hexadecimal deve ser informado.")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "O codigo hexadecimal deve estar no formato #RRGGBB.")
    String codigoHexadecimal) {
    
}
