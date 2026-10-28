package br.unitins.tp1.dto;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FornecedorDTO(
    @NotBlank(message = "A razao social deve ser informada.")
    @Size(min = 2, max = 100, message = "A razao social deve ter entre 2 e 100 caracteres.")
    String razaoSocial,
    @NotBlank(message = "O CNPJ deve ser informado.")
    @Pattern(regexp = "^\\d{14}$", message = "O CNPJ deve conter 14 digitos numericos.")
    // valida os digitos verificadores e rejeita sequencias repetidas (00000000000000)
    @CNPJ(message = "CNPJ invalido.")
    String cnpj,
    @NotBlank(message = "O telefone deve ser informado.")
    @Pattern(regexp = "^\\d{10,11}$", message = "O telefone deve conter DDD e numero, apenas digitos.")
    // DDD sem zero; fixo com 8 digitos iniciando de 2 a 5; celular com 9 digitos iniciando com 9
    @Pattern(regexp = "^(?:[1-9]{2})(?:[2-5]\\d{7}|9\\d{8})$|^(?!\\d{10,11}$).*$",
        message = "Telefone invalido: use um DDD valido e um numero fixo (8 digitos) ou celular (9 digitos iniciando com 9).")
    String telefone) {

}
