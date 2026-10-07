package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Marca;

public interface MarcaService {
    Marca create(Marca marca);
    void update(Long id, Marca marca);
    void delete(Long id);
    Marca findById(Long id);
    List<Marca> findByNome(String nome);
    List<Marca> findAll();
}
