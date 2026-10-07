package br.unitins.tp1.model;

import jakarta.persistence.Entity;

@Entity
public class CapaceteAberto extends Capacete {

    private Boolean possuiViseira;

    public Boolean getPossuiViseira() {
        return possuiViseira;
    }

    public void setPossuiViseira(Boolean possuiViseira) {
        this.possuiViseira = possuiViseira;
    }

}
