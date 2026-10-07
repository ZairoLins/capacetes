package br.unitins.tp1.repository;

import java.util.List;

import br.unitins.tp1.model.Estado;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstadoRepository implements PanacheRepository<Estado> {
    public List<Estado> findByNome(String nome) {
        // return find("SELECT e FROM Estado e WHERE e.nome LIKE ?", nome).list();
        return find("upper(nome) LIKE upper(?1)", "%" + nome + "%").list();
    }
}
