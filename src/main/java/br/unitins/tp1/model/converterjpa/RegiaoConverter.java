package br.unitins.tp1.model.converterjpa;

import br.unitins.tp1.model.Regiao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.ws.rs.BadRequestException;

@Converter(autoApply = true)
public class RegiaoConverter implements AttributeConverter <Regiao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Regiao regiao) {
        if (regiao == null) {
            return null;
        }
        return regiao.getId();
    }

    @Override
    public Regiao convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return Regiao.fromId(id);
        } catch (BadRequestException e) {
            // valor invalido vindo do banco e erro de dados do servidor, nao da requisicao (400)
            throw new IllegalStateException("Valor invalido no banco para Regiao: " + id, e);
        }
    }
    
}
