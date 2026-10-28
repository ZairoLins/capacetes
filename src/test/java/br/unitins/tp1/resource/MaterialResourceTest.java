package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.material;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.dto.MaterialDTO;
import br.unitins.tp1.dto.MaterialResponseDTO;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.service.MaterialService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class MaterialResourceTest {

    @Mock
    MaterialService service;

    @InjectMocks
    MaterialResource resource;

    @Captor
    ArgumentCaptor<Material> materialCaptor;

    @Test
    void listarRetorna200ComTodosOsMateriais() {
        when(service.findAll()).thenReturn(List.of(material(1L, "Policarbonato", "Material leve")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new MaterialResponseDTO(1L, "Policarbonato", "Material leve")), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComOMaterial() {
        when(service.findById(1L)).thenReturn(material(1L, "Policarbonato", "Material leve"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(new MaterialResponseDTO(1L, "Policarbonato", "Material leve"), response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Material nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorNomeRetorna200ComOsMateriaisEncontrados() {
        when(service.findByNome("poli")).thenReturn(List.of(material(1L, "Policarbonato", "Material leve")));

        Response response = resource.buscarPorNome("poli");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new MaterialResponseDTO(1L, "Policarbonato", "Material leve")), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Material.class))).thenAnswer(invocation -> {
            Material material = invocation.getArgument(0);
            material.setId(10L);
            return material;
        });

        Response response = resource.inserir(new MaterialDTO("Policarbonato", "Material leve"));

        verify(service).create(materialCaptor.capture());
        assertEquals("Policarbonato", materialCaptor.getValue().getNome());
        assertEquals("Material leve", materialCaptor.getValue().getDescricao());
        assertEquals(201, response.getStatus());
        assertEquals(new MaterialResponseDTO(10L, "Policarbonato", "Material leve"), response.getEntity());
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new MaterialDTO("Fibra de carbono", "Material resistente"));

        verify(service).update(eq(1L), materialCaptor.capture());
        assertEquals("Fibra de carbono", materialCaptor.getValue().getNome());
        assertEquals("Material resistente", materialCaptor.getValue().getDescricao());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
