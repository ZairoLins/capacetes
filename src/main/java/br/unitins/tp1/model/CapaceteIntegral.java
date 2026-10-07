package br.unitins.tp1.model;

import jakarta.persistence.Entity;

@Entity
public class CapaceteIntegral extends Capacete {

    private Boolean possuiViseiraSolar;

    public Boolean getPossuiViseiraSolar() {
        return possuiViseiraSolar;
    }

    public void setPossuiViseiraSolar(Boolean possuiViseiraSolar) {
        this.possuiViseiraSolar = possuiViseiraSolar;
    }

}
