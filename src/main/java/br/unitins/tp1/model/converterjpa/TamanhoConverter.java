package br.unitins.tp1.model.converterjpa;

import br.unitins.tp1.model.Tamanho;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.ws.rs.BadRequestException;

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
        try {
            return Tamanho.fromId(id);
        } catch (BadRequestException e) {
            // valor invalido vindo do banco e erro de dados do servidor, nao da requisicao (400)
            throw new IllegalStateException("Valor invalido no banco para Tamanho: " + id, e);
        }
    }
    
}
