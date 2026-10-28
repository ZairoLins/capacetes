package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.cor;
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

import br.unitins.tp1.dto.CorDTO;
import br.unitins.tp1.dto.CorResponseDTO;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.service.CorService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class CorResourceTest {

    @Mock
    CorService service;

    @InjectMocks
    CorResource resource;

    @Captor
    ArgumentCaptor<Cor> corCaptor;

    @Test
    void listarRetorna200ComTodasAsCores() {
        when(service.findAll()).thenReturn(List.of(cor(1L, "Preto", "#000000"), cor(2L, "Branco", "#FFFFFF")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(
                new CorResponseDTO(1L, "Preto", "#000000"),
                new CorResponseDTO(2L, "Branco", "#FFFFFF")), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComACor() {
        when(service.findById(1L)).thenReturn(cor(1L, "Preto", "#000000"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(new CorResponseDTO(1L, "Preto", "#000000"), response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Cor nao encontrada."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorNomeRetorna200ComAsCoresEncontradas() {
        when(service.findByNome("pre")).thenReturn(List.of(cor(1L, "Preto", "#000000")));

        Response response = resource.buscarPorNome("pre");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(new CorResponseDTO(1L, "Preto", "#000000")), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Cor.class))).thenAnswer(invocation -> {
            Cor cor = invocation.getArgument(0);
            cor.setId(10L);
            return cor;
        });

        Response response = resource.inserir(new CorDTO("Preto", "#000000"));

        verify(service).create(corCaptor.capture());
        assertEquals("Preto", corCaptor.getValue().getNome());
        assertEquals("#000000", corCaptor.getValue().getCodigoHexadecimal());
        assertEquals(201, response.getStatus());
        assertEquals(new CorResponseDTO(10L, "Preto", "#000000"), response.getEntity());
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new CorDTO("Branco", "#FFFFFF"));

        verify(service).update(eq(1L), corCaptor.capture());
        assertEquals("Branco", corCaptor.getValue().getNome());
        assertEquals("#FFFFFF", corCaptor.getValue().getCodigoHexadecimal());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
