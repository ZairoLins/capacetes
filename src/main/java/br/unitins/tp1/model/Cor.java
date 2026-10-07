package br.unitins.tp1.model;

import jakarta.persistence.Entity;

@Entity
public class Cor extends DefaultEntity {

    private String nome;
    private String codigoHexadecimal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoHexadecimal() {
        return codigoHexadecimal;
    }

    public void setCodigoHexadecimal(String codigoHexadecimal) {
        this.codigoHexadecimal = codigoHexadecimal;
    }

}
