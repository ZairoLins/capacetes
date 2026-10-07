package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteIntegral;

public interface CapaceteIntegralService {
    CapaceteIntegral create(CapaceteIntegral capaceteIntegral);
    void update(Long id, CapaceteIntegral capaceteIntegral);
    void delete(Long id);
    CapaceteIntegral findById(Long id);
    List<CapaceteIntegral> findByModelo(String modelo);
    List<CapaceteIntegral> findAll();
}
