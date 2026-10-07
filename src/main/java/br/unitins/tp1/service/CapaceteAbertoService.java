package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteAberto;

public interface CapaceteAbertoService {
    CapaceteAberto create(CapaceteAberto capaceteAberto);
    void update(Long id, CapaceteAberto capaceteAberto);
    void delete(Long id);
    CapaceteAberto findById(Long id);
    List<CapaceteAberto> findByModelo(String modelo);
    List<CapaceteAberto> findAll();
}
