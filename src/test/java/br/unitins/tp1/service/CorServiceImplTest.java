package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.cor;
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

import br.unitins.tp1.model.Cor;
import br.unitins.tp1.repository.CorRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CorServiceImplTest {

    @Mock
    CorRepository repository;

    @InjectMocks
    CorServiceImpl service;

    @Test
    void createPersisteERetornaACor() {
        Cor cor = cor(null, "Preto", "#000000");

        Cor criada = service.create(cor);

        verify(repository).persist(cor);
        assertSame(cor, criada);
    }

    @Test
    void updateCopiaOsCamposParaACorExistente() {
        Cor existente = cor(1L, "Preto", "#000000");
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, cor(null, "Branco", "#FFFFFF"));

        assertEquals(1L, existente.getId());
        assertEquals("Branco", existente.getNome());
        assertEquals("#FFFFFF", existente.getCodigoHexadecimal());
    }

    @Test
    void updateLancaNotFoundQuandoACorNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, cor(null, "Branco", "#FFFFFF")));
        assertEquals("Cor nao encontrada.", e.getMessage());
    }

    @Test
    void deleteRemoveACorExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoACorNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Cor nao encontrada.", e.getMessage());
    }

    @Test
    void findByIdRetornaACor() {
        Cor cor = cor(1L, "Preto", "#000000");
        when(repository.findById(1L)).thenReturn(cor);

        assertSame(cor, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoACorNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Cor nao encontrada.", e.getMessage());
    }

    @Test
    void findByNomeRetornaOResultadoDoRepositorio() {
        List<Cor> cores = List.of(cor(1L, "Preto", "#000000"));
        when(repository.findByNome("pre")).thenReturn(cores);

        assertSame(cores, service.findByNome("pre"));
    }

    @Test
    void findAllRetornaTodasAsCores() {
        List<Cor> cores = List.of(cor(1L, "Preto", "#000000"), cor(2L, "Branco", "#FFFFFF"));
        when(repository.listAll()).thenReturn(cores);

        assertSame(cores, service.findAll());
    }
}
