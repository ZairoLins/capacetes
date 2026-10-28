package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.categoria;
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

import br.unitins.tp1.dto.CategoriaDTO;
import br.unitins.tp1.dto.CategoriaResponseDTO;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.service.CategoriaService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class CategoriaResourceTest {

    @Mock
    CategoriaService service;

    @InjectMocks
    CategoriaResource resource;

    @Captor
    ArgumentCaptor<Categoria> categoriaCaptor;

    @Test
    void listarRetorna200ComTodasAsCategorias() {
        when(service.findAll()).thenReturn(List.of(categoria(1L, "Urbano", "Uso na cidade")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new CategoriaResponseDTO(1L, "Urbano", "Uso na cidade")), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComACategoria() {
        when(service.findById(1L)).thenReturn(categoria(1L, "Urbano", "Uso na cidade"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(new CategoriaResponseDTO(1L, "Urbano", "Uso na cidade"), response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Categoria nao encontrada."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorNomeRetorna200ComAsCategoriasEncontradas() {
        when(service.findByNome("urb")).thenReturn(List.of(categoria(1L, "Urbano", "Uso na cidade")));

        Response response = resource.buscarPorNome("urb");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new CategoriaResponseDTO(1L, "Urbano", "Uso na cidade")), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria categoria = invocation.getArgument(0);
            categoria.setId(10L);
            return categoria;
        });

        Response response = resource.inserir(new CategoriaDTO("Urbano", "Uso na cidade"));

        verify(service).create(categoriaCaptor.capture());
        assertEquals("Urbano", categoriaCaptor.getValue().getNome());
        assertEquals("Uso na cidade", categoriaCaptor.getValue().getDescricao());
        assertEquals(201, response.getStatus());
        assertEquals(new CategoriaResponseDTO(10L, "Urbano", "Uso na cidade"), response.getEntity());
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new CategoriaDTO("Esportivo", "Uso em pista"));

        verify(service).update(eq(1L), categoriaCaptor.capture());
        assertEquals("Esportivo", categoriaCaptor.getValue().getNome());
        assertEquals("Uso em pista", categoriaCaptor.getValue().getDescricao());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
