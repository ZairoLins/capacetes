package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.fornecedor;
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

import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.repository.FornecedorRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceImplTest {

    @Mock
    FornecedorRepository repository;

    @InjectMocks
    FornecedorServiceImpl service;

    @Test
    void createPersisteERetornaOFornecedor() {
        Fornecedor fornecedor = fornecedor(null, "Moto Pecas LTDA", "11222333000181", "63992345678");

        Fornecedor criado = service.create(fornecedor);

        verify(repository).persist(fornecedor);
        assertSame(fornecedor, criado);
    }

    @Test
    void updateCopiaOsCamposParaOFornecedorExistente() {
        Fornecedor existente = fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678");
        when(repository.findById(1L)).thenReturn(existente);

        service.update(1L, fornecedor(null, "Capacetes SA", "11444777000161", "6332145678"));

        assertEquals(1L, existente.getId());
        assertEquals("Capacetes SA", existente.getRazaoSocial());
        assertEquals("11444777000161", existente.getCnpj());
        assertEquals("6332145678", existente.getTelefone());
    }

    @Test
    void updateLancaNotFoundQuandoOFornecedorNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, fornecedor(null, "Capacetes SA", "11444777000161", "6332145678")));
        assertEquals("Fornecedor nao encontrado.", e.getMessage());
    }

    @Test
    void deleteRemoveOFornecedorExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoOFornecedorNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Fornecedor nao encontrado.", e.getMessage());
    }

    @Test
    void findByIdRetornaOFornecedor() {
        Fornecedor fornecedor = fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678");
        when(repository.findById(1L)).thenReturn(fornecedor);

        assertSame(fornecedor, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoOFornecedorNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Fornecedor nao encontrado.", e.getMessage());
    }

    @Test
    void findByRazaoSocialRetornaOResultadoDoRepositorio() {
        List<Fornecedor> fornecedores = List.of(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678"));
        when(repository.findByRazaoSocial("moto")).thenReturn(fornecedores);

        assertSame(fornecedores, service.findByRazaoSocial("moto"));
    }

    @Test
    void findAllRetornaTodosOsFornecedores() {
        List<Fornecedor> fornecedores = List.of(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678"));
        when(repository.listAll()).thenReturn(fornecedores);

        assertSame(fornecedores, service.findAll());
    }
}
