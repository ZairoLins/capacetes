package br.unitins.tp1.repository;

import java.util.List;
import java.util.Objects;

import br.unitins.tp1.model.Fornecedor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FornecedorRepository implements PanacheRepository<Fornecedor> {
    public List<Fornecedor> findByRazaoSocial(String razaoSocial) {
        return find("upper(razaoSocial) LIKE upper(?1)", "%" + razaoSocial + "%").list();
    }

    // idIgnorado: no update, o proprio registro nao conta como duplicado
    public boolean existsByCnpj(String cnpj, Long idIgnorado) {
        return count("cnpj = ?1 and id <> ?2", cnpj, Objects.requireNonNullElse(idIgnorado, 0L)) > 0;
    }
}
