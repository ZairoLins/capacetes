package br.unitins.tp1.model.converterjpa;

import br.unitins.tp1.model.Tamanho;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TamanhoConverter implements AttributeConverter <Tamanho, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Tamanho tamanho) {
        if (tamanho == null) {
            return null;
        }
        return tamanho.getId();
    }

    @Override
    public Tamanho convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return Tamanho.fromId(id);
    }
    
}
