package br.unitins.tp1.repository;

import java.util.List;

import br.unitins.tp1.model.Fornecedor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FornecedorRepository implements PanacheRepository<Fornecedor> {
    public List<Fornecedor> findByRazaoSocial(String razaoSocial) {
        return find("upper(razaoSocial) LIKE upper(?1)", "%" + razaoSocial + "%").list();
    }
}
