package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.estado;
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

import br.unitins.tp1.model.Estado;
import br.unitins.tp1.model.Regiao;
import br.unitins.tp1.repository.EstadoRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class EstadoServiceImplTest {

    @Mock
    EstadoRepository repository;

    @InjectMocks
    EstadoServiceImpl service;

    @Test
    void createPersisteERetornaOEstado() {
        Estado estado = estado(null, "Tocantins", "TO", Regiao.NORTE);

        Estado criado = service.create(estado);

        verify(repository).persist(estado);
        assertSame(estado, criado);
    }

    @Test
    void updateCopiaOsCamposParaOEstadoExistente() {
        Estado existente = estado(1L, "Tocantins", "TO", Regiao.NORTE);
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, estado(null, "Goias", "GO", Regiao.CENTRO_OESTE));

        assertEquals(1L, existente.getId());
        assertEquals("Goias", existente.getNome());
        assertEquals("GO", existente.getSigla());
        assertEquals(Regiao.CENTRO_OESTE, existente.getRegiao());
    }

    @Test
    void updateLancaNotFoundQuandoOEstadoNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, estado(null, "Goias", "GO", Regiao.CENTRO_OESTE)));
        assertEquals("Estado nao encontrado.", e.getMessage());
    }

    @Test
    void deleteRemoveOEstadoExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoOEstadoNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Estado nao encontrado.", e.getMessage());
    }

    @Test
    void findByIdRetornaOEstado() {
        Estado estado = estado(1L, "Tocantins", "TO", Regiao.NORTE);
        when(repository.findById(1L)).thenReturn(estado);

        assertSame(estado, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoOEstadoNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Estado nao encontrado.", e.getMessage());
    }

    @Test
    void findByNomeRetornaOResultadoDoRepositorio() {
        List<Estado> estados = List.of(estado(1L, "Tocantins", "TO", Regiao.NORTE));
        when(repository.findByNome("toc")).thenReturn(estados);

        assertSame(estados, service.findByNome("toc"));
    }

    @Test
    void findAllRetornaTodosOsEstados() {
        List<Estado> estados = List.of(estado(1L, "Tocantins", "TO", Regiao.NORTE));
        when(repository.listAll()).thenReturn(estados);

        assertSame(estados, service.findAll());
    }
}
