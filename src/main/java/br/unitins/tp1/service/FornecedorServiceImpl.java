package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.repository.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FornecedorServiceImpl implements FornecedorService {

    @Inject
    FornecedorRepository repository;

    @Override
    @Transactional
    public Fornecedor create(Fornecedor fornecedor) {
        repository.persist(fornecedor);
        return fornecedor;
    }

    @Override
    @Transactional
    public void update(Long id, Fornecedor fornecedor) {
        Fornecedor novoFornecedor = repository.findById(id);
        if (novoFornecedor == null) {
            throw new NotFoundException("Fornecedor nao encontrado.");
        }
        novoFornecedor.setRazaoSocial(fornecedor.getRazaoSocial());
        novoFornecedor.setCnpj(fornecedor.getCnpj());
        novoFornecedor.setTelefone(fornecedor.getTelefone());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Fornecedor nao encontrado.");
        }
    }

    @Override
    public Fornecedor findById(Long id) {
       Fornecedor fornecedor = repository.findById(id);
       if (fornecedor == null) {
            throw new NotFoundException("Fornecedor nao encontrado.");
       }
       return fornecedor;
    }

    @Override
    public List<Fornecedor> findByRazaoSocial(String razaoSocial) {
        return repository.findByRazaoSocial(razaoSocial);
    }

    @Override
    public List<Fornecedor> findAll() {
        return repository.listAll();
    }

}
