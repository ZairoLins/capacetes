package br.unitins.tp1.repository;

import java.util.Objects;

import br.unitins.tp1.model.Capacete;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Consultas sobre todos os tipos de capacete (integral, aberto e off-road),
 * usadas nas regras de negocio que nao dependem do tipo especifico.
 */
@ApplicationScoped
public class CapaceteRepository implements PanacheRepository<Capacete> {

    // um mesmo modelo pode existir em varios tamanhos e cores, mas cada
    // combinacao marca + modelo + tamanho + cor identifica um unico produto
    public boolean existsProduto(Capacete capacete, Long idIgnorado) {
        return count("upper(modelo) = upper(?1) and marca = ?2 and tamanho = ?3 and cor = ?4 and id <> ?5",
                capacete.getModelo(), capacete.getMarca(), capacete.getTamanho(), capacete.getCor(),
                Objects.requireNonNullElse(idIgnorado, 0L)) > 0;
    }

    public boolean existsByMarca(Long idMarca) {
        return count("marca.id", idMarca) > 0;
    }

    public boolean existsByCategoria(Long idCategoria) {
        return count("categoria.id", idCategoria) > 0;
    }

    public boolean existsByMaterial(Long idMaterial) {
        return count("material.id", idMaterial) > 0;
    }

    public boolean existsByCor(Long idCor) {
        return count("cor.id", idCor) > 0;
    }

    public boolean existsByFornecedor(Long idFornecedor) {
        return count("from Capacete c join c.fornecedores f where f.id = ?1", idFornecedor) > 0;
    }
}
