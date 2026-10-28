package br.unitins.tp1.dto;

import java.math.BigDecimal;

import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.TipoFechamento;

public record EspecificacaoCapaceteResponseDTO(
    BigDecimal peso,
    TipoFechamento tipoFechamento
) {
    public static EspecificacaoCapaceteResponseDTO fromEntity(EspecificacaoCapacete especificacao) {
        return new EspecificacaoCapaceteResponseDTO(
            especificacao.getPeso(),
            especificacao.getTipoFechamento()
        );
    }
}
