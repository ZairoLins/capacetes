package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Cor;

public interface CorService {
    Cor create(Cor cor);
    void update(Long id, Cor cor);
    void delete(Long id);
    Cor findById(Long id);
    List<Cor> findByNome(String nome);
    List<Cor> findAll();
}
