package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.repository.CapaceteRepository;
import br.unitins.tp1.repository.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FornecedorServiceImpl implements FornecedorService {

    @Inject
    FornecedorRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public Fornecedor create(Fornecedor fornecedor) {
        validar(fornecedor, null);
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
        validar(fornecedor, id);
        novoFornecedor.setRazaoSocial(fornecedor.getRazaoSocial());
        novoFornecedor.setCnpj(fornecedor.getCnpj());
        novoFornecedor.setTelefone(fornecedor.getTelefone());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Fornecedor fornecedor = findById(id);
        if (capaceteRepository.existsByFornecedor(id)) {
            throw new ConflictException("O fornecedor esta vinculado a capacetes e nao pode ser excluido.");
        }
        repository.delete(fornecedor);
    }

    private void validar(Fornecedor fornecedor, Long id) {
        if (repository.existsByCnpj(fornecedor.getCnpj(), id)) {
            throw new ConflictException("cnpj", "Ja existe um fornecedor com o CNPJ " + fornecedor.getCnpj() + ".");
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
