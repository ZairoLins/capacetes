package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Material;

public interface MaterialService {
    Material create(Material material);
    void update(Long id, Material material);
    void delete(Long id);
    Material findById(Long id);
    List<Material> findByNome(String nome);
    List<Material> findAll();
}
