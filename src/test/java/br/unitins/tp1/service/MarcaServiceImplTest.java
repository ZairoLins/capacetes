package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.marca;
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

import br.unitins.tp1.model.Marca;
import br.unitins.tp1.repository.MarcaRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MarcaServiceImplTest {

    @Mock
    MarcaRepository repository;

    @InjectMocks
    MarcaServiceImpl service;

    @Test
    void createPersisteERetornaAMarca() {
        Marca marca = marca(null, "LS2", "Espanha");

        Marca criada = service.create(marca);

        verify(repository).persist(marca);
        assertSame(marca, criada);
    }

    @Test
    void updateCopiaOsCamposParaAMarcaExistente() {
        Marca existente = marca(1L, "LS2", "Espanha");
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, marca(null, "Shoei", "Japao"));

        assertEquals(1L, existente.getId());
        assertEquals("Shoei", existente.getNome());
        assertEquals("Japao", existente.getPaisOrigem());
    }

    @Test
    void updateLancaNotFoundQuandoAMarcaNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, marca(null, "Shoei", "Japao")));
        assertEquals("Marca nao encontrada.", e.getMessage());
    }

    @Test
    void deleteRemoveAMarcaExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoAMarcaNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Marca nao encontrada.", e.getMessage());
    }

    @Test
    void findByIdRetornaAMarca() {
        Marca marca = marca(1L, "LS2", "Espanha");
        when(repository.findById(1L)).thenReturn(marca);

        assertSame(marca, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoAMarcaNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Marca nao encontrada.", e.getMessage());
    }

    @Test
    void findByNomeRetornaOResultadoDoRepositorio() {
        List<Marca> marcas = List.of(marca(1L, "LS2", "Espanha"));
        when(repository.findByNome("ls")).thenReturn(marcas);

        assertSame(marcas, service.findByNome("ls"));
    }

    @Test
    void findAllRetornaTodasAsMarcas() {
        List<Marca> marcas = List.of(marca(1L, "LS2", "Espanha"));
        when(repository.listAll()).thenReturn(marcas);

        assertSame(marcas, service.findAll());
    }
}
