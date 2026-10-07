package br.unitins.tp1.repository;

import java.util.List;
import java.util.Objects;

import br.unitins.tp1.model.Categoria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaRepository implements PanacheRepository<Categoria> {
    public List<Categoria> findByNome(String nome) {
        return find("upper(nome) LIKE upper(?1)", "%" + nome + "%").list();
    }

    // idIgnorado: no update, o proprio registro nao conta como duplicado
    public boolean existsByNome(String nome, Long idIgnorado) {
        return count("upper(nome) = upper(?1) and id <> ?2", nome, Objects.requireNonNullElse(idIgnorado, 0L)) > 0;
    }
}
