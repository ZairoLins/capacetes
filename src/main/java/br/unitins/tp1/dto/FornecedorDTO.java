package br.unitins.tp1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FornecedorDTO(
    @NotBlank(message = "A razao social deve ser informada.")
    @Size(min = 2, max = 100, message = "A razao social deve ter entre 2 e 100 caracteres.")
    String razaoSocial,
    @NotBlank(message = "O CNPJ deve ser informado.")
    @Pattern(regexp = "^\\d{14}$", message = "O CNPJ deve conter 14 digitos numericos.")
    String cnpj,
    @NotBlank(message = "O telefone deve ser informado.")
    @Pattern(regexp = "^\\d{10,11}$", message = "O telefone deve conter DDD e numero, apenas digitos.")
    String telefone) {

}
