package br.unitins.tp1.model.converterjpa;

import br.unitins.tp1.model.TipoFechamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

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
        return TipoFechamento.fromId(id);
    }
    
}
