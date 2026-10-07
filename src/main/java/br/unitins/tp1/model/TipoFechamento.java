package br.unitins.tp1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.ws.rs.BadRequestException;

@JsonFormat (shape = Shape.OBJECT)
public enum TipoFechamento {
    DUPLO_D(1, "Duplo D"),
    MICROMETRICO(2, "Micrometrico"),
    ENGATE_RAPIDO(3, "Engate Rapido");

    private final int id;
    private final String nome;

    TipoFechamento(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static TipoFechamento fromId(int id) {
        for (TipoFechamento tipoFechamento : TipoFechamento.values()) {
            if (tipoFechamento.getId() == id) {
                return tipoFechamento;
            }
        }
        throw new BadRequestException("Tipo de fechamento invalido: " + id);
    }
}
