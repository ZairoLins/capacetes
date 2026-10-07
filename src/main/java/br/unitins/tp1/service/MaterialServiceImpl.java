package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.exception.ConflictException;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.repository.CapaceteRepository;
import br.unitins.tp1.repository.MaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class MaterialServiceImpl implements MaterialService {

    @Inject
    MaterialRepository repository;

    @Inject
    CapaceteRepository capaceteRepository;

    @Override
    @Transactional
    public Material create(Material material) {
        validar(material, null);
        repository.persist(material);
        return material;
    }

    @Override
    @Transactional
    public void update(Long id, Material material) {
        Material novoMaterial = repository.findById(id);
        if (novoMaterial == null) {
            throw new NotFoundException("Material nao encontrado.");
        }
        validar(material, id);
        novoMaterial.setNome(material.getNome());
        novoMaterial.setDescricao(material.getDescricao());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Material material = findById(id);
        if (capaceteRepository.existsByMaterial(id)) {
            throw new ConflictException("O material possui capacetes cadastrados e nao pode ser excluido.");
        }
        repository.delete(material);
    }

    private void validar(Material material, Long id) {
        if (repository.existsByNome(material.getNome(), id)) {
            throw new ConflictException("nome", "Ja existe um material com o nome " + material.getNome() + ".");
        }
    }

    @Override
    public Material findById(Long id) {
       Material material = repository.findById(id);
       if (material == null) {
            throw new NotFoundException("Material nao encontrado.");
       }
       return material;
    }

    @Override
    public List<Material> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public List<Material> findAll() {
        return repository.listAll();
    }
    
}
