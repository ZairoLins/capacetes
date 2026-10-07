package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Estado;
import br.unitins.tp1.repository.EstadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EstadoServiceImpl implements EstadoService {

    @Inject
    EstadoRepository repository;

    @Override
    @Transactional
    public Estado create(Estado estado) {
        validar(estado, null);
        repository.persist(estado);
        return estado;
    }

    @Override
    @Transactional
    public void update(Long id, Estado estado) {
        Estado novoEstado = repository.findById(id);
        if (novoEstado == null) {
            throw new NotFoundException("Estado nao encontrado.");
        }
        validar(estado, id);
        novoEstado.setNome(estado.getNome());
        novoEstado.setSigla(estado.getSigla());
        novoEstado.setRegiao(estado.getRegiao());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Estado estado = findById(id);
        if (repository.possuiMunicipios(id)) {
            throw new ConflictException("O estado possui municipios cadastrados e nao pode ser excluido.");
        }
        repository.delete(estado);
    }

    private void validar(Estado estado, Long id) {
        if (repository.existsBySigla(estado.getSigla(), id)) {
            throw new ConflictException("sigla", "Ja existe um estado com a sigla " + estado.getSigla() + ".");
        }
        if (repository.existsByNome(estado.getNome(), id)) {
            throw new ConflictException("nome", "Ja existe um estado com o nome " + estado.getNome() + ".");
        }
    }

    @Override
    public Estado findById(Long id) {
       Estado estado = repository.findById(id);
       if (estado == null) {
            throw new NotFoundException("Estado nao encontrado.");
       }
       return estado;
    }

    @Override
    public List<Estado> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Estado> findAll() {
        return repository.listAll();
    }
    
}
