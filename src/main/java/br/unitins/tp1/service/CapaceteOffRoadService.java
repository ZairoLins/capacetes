package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.CapaceteOffRoad;

public interface CapaceteOffRoadService {
    CapaceteOffRoad create(CapaceteOffRoad capaceteOffRoad);
    void update(Long id, CapaceteOffRoad capaceteOffRoad);
    void delete(Long id);
    CapaceteOffRoad findById(Long id);
    List<CapaceteOffRoad> findByModelo(String modelo);
    List<CapaceteOffRoad> findAll();
}
