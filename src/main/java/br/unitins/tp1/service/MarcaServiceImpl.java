package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Marca;
import br.unitins.tp1.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    @Inject
    MarcaRepository repository;

    @Override
    @Transactional
    public Marca create(Marca marca) {
        repository.persist(marca);
        return marca;
    }

    @Override
    @Transactional
    public void update(Long id, Marca marca) {
        Marca novoMarca = repository.findById(id);
        if (novoMarca == null) {
            throw new NotFoundException("Marca nao encontrada.");
        }
        novoMarca.setNome(marca.getNome());
        novoMarca.setPaisOrigem(marca.getPaisOrigem());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Marca nao encontrada.");
        }
    }

    @Override
    public Marca findById(Long id) {
       Marca marca = repository.findById(id);
       if (marca == null) {
            throw new NotFoundException("Marca nao encontrada.");
       }
       return marca;
    }

    @Override
    public List<Marca> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Marca> findAll() {
        return repository.listAll();
    }
    
}
