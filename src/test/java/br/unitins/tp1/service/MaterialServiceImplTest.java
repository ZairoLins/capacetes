package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.material;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.model.Material;
import br.unitins.tp1.repository.MaterialRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MaterialServiceImplTest {

    @Mock
    MaterialRepository repository;

    @InjectMocks
    MaterialServiceImpl service;

    @Test
    void createPersisteERetornaOMaterial() {
        Material material = material(null, "Policarbonato", "Material leve");

        Material criado = service.create(material);

        verify(repository).persist(material);
        assertSame(material, criado);
    }

    @Test
    void updateCopiaOsCamposParaOMaterialExistente() {
        Material existente = material(1L, "Policarbonato", "Material leve");
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, material(null, "Fibra de carbono", "Material resistente"));

        assertEquals(1L, existente.getId());
        assertEquals("Fibra de carbono", existente.getNome());
        assertEquals("Material resistente", existente.getDescricao());
    }

    @Test
    void updateLancaNotFoundQuandoOMaterialNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, material(null, "Fibra de carbono", "Material resistente")));
        assertEquals("Material nao encontrado.", e.getMessage());
    }

    @Test
    void deleteRemoveOMaterialExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoOMaterialNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Material nao encontrado.", e.getMessage());
    }

    @Test
    void findByIdRetornaOMaterial() {
        Material material = material(1L, "Policarbonato", "Material leve");
        when(repository.findById(1L)).thenReturn(material);

        assertSame(material, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoOMaterialNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Material nao encontrado.", e.getMessage());
    }

    @Test
    void findByNomeRetornaOResultadoDoRepositorio() {
        List<Material> materiais = List.of(material(1L, "Policarbonato", "Material leve"));
        when(repository.findByNome("poli")).thenReturn(materiais);

        assertSame(materiais, service.findByNome("poli"));
    }

    @Test
    void findAllRetornaTodosOsMateriais() {
        List<Material> materiais = List.of(material(1L, "Policarbonato", "Material leve"));
        when(repository.listAll()).thenReturn(materiais);

        assertSame(materiais, service.findAll());
    }
}
