package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.repository.CapaceteOffRoadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CapaceteOffRoadServiceImpl implements CapaceteOffRoadService {

    @Inject
    CapaceteOffRoadRepository repository;

    @Override
    @Transactional
    public CapaceteOffRoad create(CapaceteOffRoad capaceteOffRoad) {
        repository.persist(capaceteOffRoad);
        return capaceteOffRoad;
    }

    @Override
    @Transactional
    public void update(Long id, CapaceteOffRoad capaceteOffRoad) {
        CapaceteOffRoad novoCapacete = repository.findById(id);
        if (novoCapacete == null) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
        novoCapacete.setModelo(capaceteOffRoad.getModelo());
        novoCapacete.setPreco(capaceteOffRoad.getPreco());
        novoCapacete.setTamanho(capaceteOffRoad.getTamanho());
        novoCapacete.setQuantidadeEstoque(capaceteOffRoad.getQuantidadeEstoque());
        novoCapacete.setMarca(capaceteOffRoad.getMarca());
        novoCapacete.setCategoria(capaceteOffRoad.getCategoria());
        novoCapacete.setMaterial(capaceteOffRoad.getMaterial());
        novoCapacete.setCor(capaceteOffRoad.getCor());

        // composicao: a especificacao pertence ao capacete, entao e atualizada junto com ele
        novoCapacete.getEspecificacao().setPeso(capaceteOffRoad.getEspecificacao().getPeso());
        novoCapacete.getEspecificacao().setTipoFechamento(capaceteOffRoad.getEspecificacao().getTipoFechamento());

        novoCapacete.setFornecedores(capaceteOffRoad.getFornecedores());

        novoCapacete.setPossuiPala(capaceteOffRoad.getPossuiPala());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
    }

    @Override
    public CapaceteOffRoad findById(Long id) {
       CapaceteOffRoad capaceteOffRoad = repository.findById(id);
       if (capaceteOffRoad == null) {
            throw new NotFoundException("Capacete nao encontrado.");
       }
       return capaceteOffRoad;
    }

    @Override
    public List<CapaceteOffRoad> findByModelo(String modelo) {
        return repository.findByModelo(modelo);
    }

    @Override
    public List<CapaceteOffRoad> findAll() {
        return repository.listAll();
    }

}
