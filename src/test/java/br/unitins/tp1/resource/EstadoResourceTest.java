package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.estado;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.dto.EstadoDTO;
import br.unitins.tp1.dto.EstadoResponseDTO;
import br.unitins.tp1.model.Estado;
import br.unitins.tp1.model.Regiao;
import br.unitins.tp1.service.EstadoService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class EstadoResourceTest {

    @Mock
    EstadoService service;

    @InjectMocks
    EstadoResource resource;

    @Captor
    ArgumentCaptor<Estado> estadoCaptor;

    @Test
    void listarRetorna200ComTodosOsEstados() {
        when(service.findAll()).thenReturn(List.of(estado(1L, "Tocantins", "TO", Regiao.NORTE)));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new EstadoResponseDTO(1L, "Tocantins", "TO", Regiao.NORTE)), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComOEstado() {
        when(service.findById(1L)).thenReturn(estado(1L, "Tocantins", "TO", Regiao.NORTE));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(new EstadoResponseDTO(1L, "Tocantins", "TO", Regiao.NORTE), response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Estado nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorNomeRetorna200ComOsEstadosEncontrados() {
        when(service.findByNome("toc")).thenReturn(List.of(estado(1L, "Tocantins", "TO", Regiao.NORTE)));

        Response response = resource.buscarPorNome("toc");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new EstadoResponseDTO(1L, "Tocantins", "TO", Regiao.NORTE)), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Estado.class))).thenAnswer(invocation -> {
            Estado estado = invocation.getArgument(0);
            estado.setId(10L);
            return estado;
        });

        Response response = resource.inserir(new EstadoDTO("Tocantins", "TO", 3));

        verify(service).create(estadoCaptor.capture());
        assertEquals("Tocantins", estadoCaptor.getValue().getNome());
        assertEquals("TO", estadoCaptor.getValue().getSigla());
        assertEquals(Regiao.NORTE, estadoCaptor.getValue().getRegiao());
        assertEquals(201, response.getStatus());
        assertEquals(new EstadoResponseDTO(10L, "Tocantins", "TO", Regiao.NORTE), response.getEntity());
    }

    @Test
    void inserirComRegiaoInexistenteLancaBadRequestSemChamarOService() {
        assertThrows(BadRequestException.class, () -> resource.inserir(new EstadoDTO("Tocantins", "TO", 9)));

        verifyNoInteractions(service);
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new EstadoDTO("Goias", "GO", 1));

        verify(service).update(eq(1L), estadoCaptor.capture());
        assertEquals("Goias", estadoCaptor.getValue().getNome());
        assertEquals("GO", estadoCaptor.getValue().getSigla());
        assertEquals(Regiao.CENTRO_OESTE, estadoCaptor.getValue().getRegiao());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
