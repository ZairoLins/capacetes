package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Cor;
import br.unitins.tp1.repository.CorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CorServiceImpl implements CorService {

    @Inject
    CorRepository repository;

    @Override
    @Transactional
    public Cor create(Cor cor) {
        repository.persist(cor);
        return cor;
    }

    @Override
    @Transactional
    public void update(Long id, Cor cor) {
        Cor novoCor = repository.findById(id);
        if (novoCor == null) {
            throw new NotFoundException("Cor nao encontrada.");
        }
        novoCor.setNome(cor.getNome());
        novoCor.setCodigoHexadecimal(cor.getCodigoHexadecimal());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Cor nao encontrada.");
        }
    }

    @Override
    public Cor findById(Long id) {
       Cor cor = repository.findById(id);
       if (cor == null) {
            throw new NotFoundException("Cor nao encontrada.");
       }
       return cor;
    }

    @Override
    public List<Cor> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Cor> findAll() {
        return repository.listAll();
    }
    
}
