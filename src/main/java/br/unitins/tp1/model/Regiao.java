package br.unitins.tp1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.ws.rs.BadRequestException;

@JsonFormat (shape = Shape.OBJECT)
public enum Regiao {
    CENTRO_OESTE(1, "Centro-Oeste"),
    NORDESTE(2, "Nordeste"),
    NORTE(3, "Norte"),
    SUDESTE(4, "Sudeste"),
    SUL(5, "Sul");

    private final int id;
    private final String nome;

    Regiao(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static Regiao fromId(int id) {
        for (Regiao regiao : Regiao.values()) {
            if (regiao.getId() == id) {
                return regiao;
            }
        }
        throw new BadRequestException("Regiao invalida: " + id);
    }
}