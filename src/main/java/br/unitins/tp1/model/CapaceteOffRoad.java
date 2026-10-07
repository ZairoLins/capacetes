package br.unitins.tp1.model;

import jakarta.persistence.Entity;

@Entity
public class CapaceteOffRoad extends Capacete {

    private Boolean possuiPala;

    public Boolean getPossuiPala() {
        return possuiPala;
    }

    public void setPossuiPala(Boolean possuiPala) {
        this.possuiPala = possuiPala;
    }

}
