package br.unitins.tp1.repository;

import java.util.List;
import java.util.Objects;

import br.unitins.tp1.model.Estado;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstadoRepository implements PanacheRepository<Estado> {
    public List<Estado> findByNome(String nome) {
        // return find("SELECT e FROM Estado e WHERE e.nome LIKE ?", nome).list();
        return find("upper(nome) LIKE upper(?1)", "%" + nome + "%").list();
    }

    // idIgnorado: no update, o proprio registro nao conta como duplicado
    public boolean existsByNome(String nome, Long idIgnorado) {
        return count("upper(nome) = upper(?1) and id <> ?2", nome, Objects.requireNonNullElse(idIgnorado, 0L)) > 0;
    }

    public boolean existsBySigla(String sigla, Long idIgnorado) {
        return count("upper(sigla) = upper(?1) and id <> ?2", sigla, Objects.requireNonNullElse(idIgnorado, 0L)) > 0;
    }

    public boolean possuiMunicipios(Long idEstado) {
        return getEntityManager()
                .createQuery("select count(m) from Municipio m where m.estado.id = ?1", Long.class)
                .setParameter(1, idEstado)
                .getSingleResult() > 0;
    }
}
