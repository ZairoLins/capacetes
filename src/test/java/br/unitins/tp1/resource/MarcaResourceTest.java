package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.marca;
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

import br.unitins.tp1.dto.MarcaDTO;
import br.unitins.tp1.dto.MarcaResponseDTO;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.service.MarcaService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class MarcaResourceTest {

    @Mock
    MarcaService service;

    @InjectMocks
    MarcaResource resource;

    @Captor
    ArgumentCaptor<Marca> marcaCaptor;

    @Test
    void listarRetorna200ComTodasAsMarcas() {
        when(service.findAll()).thenReturn(List.of(marca(1L, "LS2", "Espanha"), marca(2L, "Shoei", "Japao")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(
                new MarcaResponseDTO(1L, "LS2", "Espanha"),
                new MarcaResponseDTO(2L, "Shoei", "Japao")), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComAMarca() {
        when(service.findById(1L)).thenReturn(marca(1L, "LS2", "Espanha"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(new MarcaResponseDTO(1L, "LS2", "Espanha"), response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Marca nao encontrada."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorNomeRetorna200ComAsMarcasEncontradas() {
        when(service.findByNome("ls")).thenReturn(List.of(marca(1L, "LS2", "Espanha")));

        Response response = resource.buscarPorNome("ls");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new MarcaResponseDTO(1L, "LS2", "Espanha")), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Marca.class))).thenAnswer(invocation -> {
            Marca marca = invocation.getArgument(0);
            marca.setId(10L);
            return marca;
        });

        Response response = resource.inserir(new MarcaDTO("LS2", "Espanha"));

        verify(service).create(marcaCaptor.capture());
        assertEquals("LS2", marcaCaptor.getValue().getNome());
        assertEquals("Espanha", marcaCaptor.getValue().getPaisOrigem());
        assertEquals(201, response.getStatus());
        assertEquals(new MarcaResponseDTO(10L, "LS2", "Espanha"), response.getEntity());
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new MarcaDTO("Shoei", "Japao"));

        verify(service).update(eq(1L), marcaCaptor.capture());
        assertEquals("Shoei", marcaCaptor.getValue().getNome());
        assertEquals("Japao", marcaCaptor.getValue().getPaisOrigem());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
