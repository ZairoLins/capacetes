package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.repository.CapaceteRepository;
import br.unitins.tp1.repository.CorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CorServiceImpl implements CorService {

    @Inject
    CorRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public Cor create(Cor cor) {
        validar(cor, null);
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
        validar(cor, id);
        novoCor.setNome(cor.getNome());
        novoCor.setCodigoHexadecimal(cor.getCodigoHexadecimal());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cor cor = findById(id);
        if (capaceteRepository.existsByCor(id)) {
            throw new ConflictException("A cor possui capacetes cadastrados e nao pode ser excluida.");
        }
        repository.delete(cor);
    }

    private void validar(Cor cor, Long id) {
        if (repository.existsByNome(cor.getNome(), id)) {
            throw new ConflictException("nome", "Ja existe uma cor com o nome " + cor.getNome() + ".");
        }
        if (repository.existsByCodigoHexadecimal(cor.getCodigoHexadecimal(), id)) {
            throw new ConflictException("codigoHexadecimal",
                    "Ja existe uma cor com o codigo " + cor.getCodigoHexadecimal() + ".");
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
