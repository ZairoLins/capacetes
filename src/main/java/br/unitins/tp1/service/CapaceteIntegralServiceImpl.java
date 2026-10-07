package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.repository.CapaceteIntegralRepository;
import br.unitins.tp1.repository.CapaceteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CapaceteIntegralServiceImpl implements CapaceteIntegralService {

    @Inject
    CapaceteIntegralRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public CapaceteIntegral create(CapaceteIntegral capaceteIntegral) {
        validar(capaceteIntegral, null);
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
        validar(capaceteIntegral, id);
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

    private void validar(CapaceteIntegral capaceteIntegral, Long id) {
        if (capaceteRepository.existsProduto(capaceteIntegral, id)) {
            throw new ConflictException("modelo", "Ja existe um capacete " + capaceteIntegral.getModelo()
                    + " da marca " + capaceteIntegral.getMarca().getNome()
                    + " no tamanho " + capaceteIntegral.getTamanho().getNome()
                    + " e cor " + capaceteIntegral.getCor().getNome() + ".");
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
