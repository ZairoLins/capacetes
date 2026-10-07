package br.unitins.tp1.repository;

import java.util.List;

import br.unitins.tp1.model.CapaceteOffRoad;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CapaceteOffRoadRepository implements PanacheRepository<CapaceteOffRoad> {
    public List<CapaceteOffRoad> findByModelo(String modelo) {
        return find("upper(modelo) LIKE upper(?1)", "%" + modelo + "%").list();
    }
}
