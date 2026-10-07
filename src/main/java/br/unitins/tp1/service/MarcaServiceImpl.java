package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.repository.CapaceteRepository;
import br.unitins.tp1.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    @Inject
    MarcaRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public Marca create(Marca marca) {
        validar(marca, null);
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
        validar(marca, id);
        novoMarca.setNome(marca.getNome());
        novoMarca.setPaisOrigem(marca.getPaisOrigem());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Marca marca = findById(id);
        if (capaceteRepository.existsByMarca(id)) {
            throw new ConflictException("A marca possui capacetes cadastrados e nao pode ser excluida.");
        }
        repository.delete(marca);
    }

    private void validar(Marca marca, Long id) {
        if (repository.existsByNome(marca.getNome(), id)) {
            throw new ConflictException("nome", "Ja existe uma marca com o nome " + marca.getNome() + ".");
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
