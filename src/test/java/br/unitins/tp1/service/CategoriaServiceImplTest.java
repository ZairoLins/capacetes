package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.categoria;
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

import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.repository.CategoriaRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    CategoriaRepository repository;

    @InjectMocks
    CategoriaServiceImpl service;

    @Test
    void createPersisteERetornaACategoria() {
        Categoria categoria = categoria(null, "Urbano", "Uso na cidade");

        Categoria criada = service.create(categoria);

        verify(repository).persist(categoria);
        assertSame(categoria, criada);
    }

    @Test
    void updateCopiaOsCamposParaACategoriaExistente() {
        Categoria existente = categoria(1L, "Urbano", "Uso na cidade");
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, categoria(null, "Esportivo", "Uso em pista"));

        assertEquals(1L, existente.getId());
        assertEquals("Esportivo", existente.getNome());
        assertEquals("Uso em pista", existente.getDescricao());
    }

    @Test
    void updateLancaNotFoundQuandoACategoriaNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, categoria(null, "Esportivo", "Uso em pista")));
        assertEquals("Categoria nao encontrada.", e.getMessage());
    }

    @Test
    void deleteRemoveACategoriaExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoACategoriaNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Categoria nao encontrada.", e.getMessage());
    }

    @Test
    void findByIdRetornaACategoria() {
        Categoria categoria = categoria(1L, "Urbano", "Uso na cidade");
        when(repository.findById(1L)).thenReturn(categoria);

        assertSame(categoria, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoACategoriaNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Categoria nao encontrada.", e.getMessage());
    }

    @Test
    void findByNomeRetornaOResultadoDoRepositorio() {
        List<Categoria> categorias = List.of(categoria(1L, "Urbano", "Uso na cidade"));
        when(repository.findByNome("urb")).thenReturn(categorias);

        assertSame(categorias, service.findByNome("urb"));
    }

    @Test
    void findAllRetornaTodasAsCategorias() {
        List<Categoria> categorias = List.of(categoria(1L, "Urbano", "Uso na cidade"));
        when(repository.listAll()).thenReturn(categorias);

        assertSame(categorias, service.findAll());
    }
}
