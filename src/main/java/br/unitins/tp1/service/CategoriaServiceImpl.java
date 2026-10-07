package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.repository.CapaceteRepository;
import br.unitins.tp1.repository.CategoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CategoriaServiceImpl implements CategoriaService {

    @Inject
    CategoriaRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public Categoria create(Categoria categoria) {
        validar(categoria, null);
        repository.persist(categoria);
        return categoria;
    }

    @Override
    @Transactional
    public void update(Long id, Categoria categoria) {
        Categoria novoCategoria = repository.findById(id);
        if (novoCategoria == null) {
            throw new NotFoundException("Categoria nao encontrada.");
        }
        validar(categoria, id);
        novoCategoria.setNome(categoria.getNome());
        novoCategoria.setDescricao(categoria.getDescricao());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Categoria categoria = findById(id);
        if (capaceteRepository.existsByCategoria(id)) {
            throw new ConflictException("A categoria possui capacetes cadastrados e nao pode ser excluida.");
        }
        repository.delete(categoria);
    }

    private void validar(Categoria categoria, Long id) {
        if (repository.existsByNome(categoria.getNome(), id)) {
            throw new ConflictException("nome", "Ja existe uma categoria com o nome " + categoria.getNome() + ".");
        }
    }

    @Override
    public Categoria findById(Long id) {
       Categoria categoria = repository.findById(id);
       if (categoria == null) {
            throw new NotFoundException("Categoria nao encontrada.");
       }
       return categoria;
    }

    @Override
    public List<Categoria> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Categoria> findAll() {
        return repository.listAll();
    }
    
}
