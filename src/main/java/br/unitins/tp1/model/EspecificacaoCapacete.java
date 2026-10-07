package br.unitins.tp1.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EspecificacaoCapacete extends DefaultEntity {

    private BigDecimal peso;
    @Column(name = "idTipoFechamento")
    private TipoFechamento tipoFechamento;

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public TipoFechamento getTipoFechamento() {
        return tipoFechamento;
    }

    public void setTipoFechamento(TipoFechamento tipoFechamento) {
        this.tipoFechamento = tipoFechamento;
    }

}
