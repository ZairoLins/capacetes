package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.repository.CapaceteIntegralRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CapaceteIntegralServiceImpl implements CapaceteIntegralService {

    @Inject
    CapaceteIntegralRepository repository;

    @Override
    @Transactional
    public CapaceteIntegral create(CapaceteIntegral capaceteIntegral) {
        repository.persist(capaceteIntegral);
        return capaceteIntegral;
    }

    @Override
    @Transactional
    public void update(Long id, CapaceteIntegral capaceteIntegral) {
        CapaceteIntegral novoCapacete = repository.findById(id);
        if (novoCapacete == null) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
        novoCapacete.setModelo(capaceteIntegral.getModelo());
        novoCapacete.setPreco(capaceteIntegral.getPreco());
        novoCapacete.setTamanho(capaceteIntegral.getTamanho());
        novoCapacete.setQuantidadeEstoque(capaceteIntegral.getQuantidadeEstoque());
        novoCapacete.setMarca(capaceteIntegral.getMarca());
        novoCapacete.setCategoria(capaceteIntegral.getCategoria());
        novoCapacete.setMaterial(capaceteIntegral.getMaterial());
        novoCapacete.setCor(capaceteIntegral.getCor());

        // composicao: a especificacao pertence ao capacete, entao e atualizada junto com ele
        novoCapacete.getEspecificacao().setPeso(capaceteIntegral.getEspecificacao().getPeso());
        novoCapacete.getEspecificacao().setTipoFechamento(capaceteIntegral.getEspecificacao().getTipoFechamento());

        novoCapacete.setFornecedores(capaceteIntegral.getFornecedores());

        novoCapacete.setPossuiViseiraSolar(capaceteIntegral.getPossuiViseiraSolar());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Capacete nao encontrado.");
        }
    }

    @Override
    public CapaceteIntegral findById(Long id) {
       CapaceteIntegral capaceteIntegral = repository.findById(id);
       if (capaceteIntegral == null) {
            throw new NotFoundException("Capacete nao encontrado.");
       }
       return capaceteIntegral;
    }

    @Override
    public List<CapaceteIntegral> findByModelo(String modelo) {
        return repository.findByModelo(modelo);
    }

    @Override
    public List<CapaceteIntegral> findAll() {
        return repository.listAll();
    }

}
