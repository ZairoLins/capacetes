package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.repository.CapaceteAbertoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CapaceteAbertoServiceImpl implements CapaceteAbertoService {

    @Inject
    CapaceteAbertoRepository repository;

    @Override
    @Transactional
    public CapaceteAberto create(CapaceteAberto capaceteAberto) {
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
