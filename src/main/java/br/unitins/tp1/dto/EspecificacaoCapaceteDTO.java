package br.unitins.tp1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EspecificacaoCapaceteDTO(
    @NotNull(message = "O peso deve ser informado.")
    @Positive(message = "O peso deve ser maior que zero.")
    BigDecimal peso,
    @NotNull(message = "O tipo de fechamento deve ser informado.")
    @Min(value = 1, message = "Tipo de fechamento invalido.")
    @Max(value = 3, message = "Tipo de fechamento invalido.")
    Integer idTipoFechamento) {

}
