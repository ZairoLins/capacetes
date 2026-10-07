package br.unitins.tp1.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.ws.rs.BadRequestException;

@JsonFormat (shape = Shape.OBJECT)
public enum Tamanho {
    XS(1, "XS"),
    S(2, "S"),
    M(3, "M"),
    L(4, "L"),
    XL(5, "XL"),
    XXL(6, "XXL");

    private final int id;
    private final String nome;

    Tamanho(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static Tamanho fromId(int id) {
        for (Tamanho tamanho : Tamanho.values()) {
            if (tamanho.getId() == id) {
                return tamanho;
            }
        }
        throw new BadRequestException("Tamanho invalido: " + id);
    }
}
