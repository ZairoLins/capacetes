package br.unitins.tp1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EstadoDTO(
    @NotBlank(message = "O nome deve ser informado.")
    @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres.")
    String nome,
    @NotBlank(message = "A sigla deve ser informada.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "A sigla deve conter 2 letras maiusculas.")
    String sigla,
    @NotNull(message = "A regiao deve ser informada.")
    @Min(value = 1, message = "Regiao invalida.")
    @Max(value = 5, message = "Regiao invalida.")
    Integer idRegiao) {
    
}
