package br.unitins.tp1.model.converterjpa;

import br.unitins.tp1.model.TipoFechamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.ws.rs.BadRequestException;

@Converter(autoApply = true)
public class TipoFechamentoConverter implements AttributeConverter <TipoFechamento, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoFechamento tipoFechamento) {
        if (tipoFechamento == null) {
            return null;
        }
        return tipoFechamento.getId();
    }

    @Override
    public TipoFechamento convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return TipoFechamento.fromId(id);
        } catch (BadRequestException e) {
            // valor invalido vindo do banco e erro de dados do servidor, nao da requisicao (400)
            throw new IllegalStateException("Valor invalido no banco para TipoFechamento: " + id, e);
        }
    }
    
}
