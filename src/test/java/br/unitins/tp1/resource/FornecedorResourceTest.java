package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.fornecedor;
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

import br.unitins.tp1.dto.FornecedorDTO;
import br.unitins.tp1.dto.FornecedorResponseDTO;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.service.FornecedorService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class FornecedorResourceTest {

    private static final FornecedorResponseDTO RESPOSTA =
            new FornecedorResponseDTO(1L, "Moto Pecas LTDA", "11222333000181", "63992345678");

    @Mock
    FornecedorService service;

    @InjectMocks
    FornecedorResource resource;

    @Captor
    ArgumentCaptor<Fornecedor> fornecedorCaptor;

    @Test
    void listarRetorna200ComTodosOsFornecedores() {
        when(service.findAll()).thenReturn(List.of(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        assertEquals(List.of(RESPOSTA), response.getEntity());
    }

    @Test
    void buscarPorIdRetorna200ComOFornecedor() {
        when(service.findById(1L)).thenReturn(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        assertEquals(RESPOSTA, response.getEntity());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Fornecedor nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorRazaoSocialRetorna200ComOsFornecedoresEncontrados() {
        when(service.findByRazaoSocial("moto"))
                .thenReturn(List.of(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678")));

        Response response = resource.buscarPorRazaoSocial("moto");

        assertEquals(200, response.getStatus());
        assertEquals(List.of(RESPOSTA), response.getEntity());
    }

    @Test
    void inserirConverteODtoERetorna201() {
        when(service.create(any(Fornecedor.class))).thenAnswer(invocation -> {
            Fornecedor fornecedor = invocation.getArgument(0);
            fornecedor.setId(1L);
            return fornecedor;
        });

        Response response = resource.inserir(new FornecedorDTO("Moto Pecas LTDA", "11222333000181", "63992345678"));

        verify(service).create(fornecedorCaptor.capture());
        assertEquals("Moto Pecas LTDA", fornecedorCaptor.getValue().getRazaoSocial());
        assertEquals("11222333000181", fornecedorCaptor.getValue().getCnpj());
        assertEquals("63992345678", fornecedorCaptor.getValue().getTelefone());
        assertEquals(201, response.getStatus());
        assertEquals(RESPOSTA, response.getEntity());
    }

    @Test
    void atualizarConverteODtoERetorna204() {
        Response response = resource.atualizar(1L, new FornecedorDTO("Capacetes SA", "11444777000161", "6332145678"));

        verify(service).update(eq(1L), fornecedorCaptor.capture());
        assertEquals("Capacetes SA", fornecedorCaptor.getValue().getRazaoSocial());
        assertEquals("11444777000161", fornecedorCaptor.getValue().getCnpj());
        assertEquals("6332145678", fornecedorCaptor.getValue().getTelefone());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }
}
