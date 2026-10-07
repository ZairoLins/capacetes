package br.unitins.tp1.repository;

import java.util.List;

import br.unitins.tp1.model.CapaceteAberto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CapaceteAbertoRepository implements PanacheRepository<CapaceteAberto> {
    public List<CapaceteAberto> findByModelo(String modelo) {
        return find("upper(modelo) LIKE upper(?1)", "%" + modelo + "%").list();
    }
}
