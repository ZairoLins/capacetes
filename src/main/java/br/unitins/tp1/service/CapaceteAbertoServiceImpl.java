package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.repository.CapaceteAbertoRepository;
import br.unitins.tp1.repository.CapaceteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CapaceteAbertoServiceImpl implements CapaceteAbertoService {

    @Inject
    CapaceteAbertoRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public CapaceteAberto create(CapaceteAberto capaceteAberto) {
        validar(capaceteAberto, null);
        repository.persist(capaceteAberto);
        return capaceteAberto;
    }

    @Override
    @Transactional
    public void update(Long id, CapaceteAberto capaceteAberto) {
        CapaceteAberto novoCapacete = repository.findById(id);
        if (novoCapacete == null) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
        validar(capaceteAberto, id);
        novoCapacete.setModelo(capaceteAberto.getModelo());
        novoCapacete.setPreco(capaceteAberto.getPreco());
        novoCapacete.setTamanho(capaceteAberto.getTamanho());
        novoCapacete.setQuantidadeEstoque(capaceteAberto.getQuantidadeEstoque());
        novoCapacete.setMarca(capaceteAberto.getMarca());
        novoCapacete.setCategoria(capaceteAberto.getCategoria());
        novoCapacete.setMaterial(capaceteAberto.getMaterial());
        novoCapacete.setCor(capaceteAberto.getCor());

        // composicao: a especificacao pertence ao capacete, entao e atualizada junto com ele
        novoCapacete.getEspecificacao().setPeso(capaceteAberto.getEspecificacao().getPeso());
        novoCapacete.getEspecificacao().setTipoFechamento(capaceteAberto.getEspecificacao().getTipoFechamento());

        novoCapacete.setFornecedores(capaceteAberto.getFornecedores());

        novoCapacete.setPossuiViseira(capaceteAberto.getPossuiViseira());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
    }

    private void validar(CapaceteAberto capaceteAberto, Long id) {
        if (capaceteRepository.existsProduto(capaceteAberto, id)) {
            throw new ConflictException("modelo", "Ja existe um capacete " + capaceteAberto.getModelo()
                    + " da marca " + capaceteAberto.getMarca().getNome()
                    + " no tamanho " + capaceteAberto.getTamanho().getNome()
                    + " e cor " + capaceteAberto.getCor().getNome() + ".");
        }
    }

    @Override
    public CapaceteAberto findById(Long id) {
       CapaceteAberto capaceteAberto = repository.findById(id);
       if (capaceteAberto == null) {
            throw new NotFoundException("Capacete nao encontrado.");
       }
       return capaceteAberto;
    }

    @Override
    public List<CapaceteAberto> findByModelo(String modelo) {
        return repository.findByModelo(modelo);
    }

    @Override
    public List<CapaceteAberto> findAll() {
        return repository.listAll();
    }

}
