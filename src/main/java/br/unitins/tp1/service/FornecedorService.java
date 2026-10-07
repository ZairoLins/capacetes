package br.unitins.tp1.service;

import java.util.List;

import br.unitins.tp1.model.Fornecedor;

public interface FornecedorService {
    Fornecedor create(Fornecedor fornecedor);
    void update(Long id, Fornecedor fornecedor);
    void delete(Long id);
    Fornecedor findById(Long id);
    List<Fornecedor> findByRazaoSocial(String razaoSocial);
    List<Fornecedor> findAll();
}
