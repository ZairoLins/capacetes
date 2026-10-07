package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Categoria;

public interface CategoriaService {
    Categoria create(Categoria categoria);
    void update(Long id, Categoria categoria);
    void delete(Long id);
    Categoria findById(Long id);
    List<Categoria> findByNome(String nome);
    List<Categoria> findAll();
}
