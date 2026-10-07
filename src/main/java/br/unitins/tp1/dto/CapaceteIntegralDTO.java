package br.unitins.tp1.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CapaceteIntegralDTO(
    @NotBlank(message = "O modelo deve ser informado.")
    @Size(min = 2, max = 60, message = "O modelo deve ter entre 2 e 60 caracteres.")
    String modelo,
    @NotNull(message = "O preco deve ser informado.")
    @Positive(message = "O preco deve ser maior que zero.")
    BigDecimal preco,
    @NotNull(message = "O tamanho deve ser informado.")
    @Min(value = 1, message = "Tamanho invalido.")
    @Max(value = 6, message = "Tamanho invalido.")
    Integer idTamanho,
    @NotNull(message = "A quantidade em estoque deve ser informada.")
    @PositiveOrZero(message = "A quantidade em estoque nao pode ser negativa.")
    Integer quantidadeEstoque,
    @NotNull(message = "A marca deve ser informada.")
    @Positive(message = "Marca invalida.")
    Long idMarca,
    @NotNull(message = "A categoria deve ser informada.")
    @Positive(message = "Categoria invalida.")
    Long idCategoria,
    @NotNull(message = "O material deve ser informado.")
    @Positive(message = "Material invalido.")
    Long idMaterial,
    @NotNull(message = "A cor deve ser informada.")
    @Positive(message = "Cor invalida.")
    Long idCor,
    @NotNull(message = "A especificacao deve ser informada.")
    @Valid
    EspecificacaoCapaceteDTO especificacao,
    List<Long> idFornecedores,
    @NotNull(message = "Deve ser informado se possui viseira solar.")
    Boolean possuiViseiraSolar) {

}
