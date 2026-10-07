package br.unitins.tp1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EspecificacaoCapaceteDTO(
    // peso em kg: capacetes de moto ficam, na pratica, entre 0,5 e 3 kg
    @NotNull(message = "O peso deve ser informado.")
    @DecimalMin(value = "0.5", message = "O peso deve ser de no minimo 0,5 kg.")
    @DecimalMax(value = "3.0", message = "O peso deve ser de no maximo 3 kg.")
    @Digits(integer = 1, fraction = 3, message = "O peso deve ter no maximo 3 casas decimais.")
    BigDecimal peso,
    @NotNull(message = "O tipo de fechamento deve ser informado.")
    @Min(value = 1, message = "Tipo de fechamento invalido.")
    @Max(value = 3, message = "Tipo de fechamento invalido.")
    Integer idTipoFechamento) {

}
