package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.capaceteAberto;
import static br.unitins.tp1.Fixtures.categoria;
import static br.unitins.tp1.Fixtures.cor;
import static br.unitins.tp1.Fixtures.fornecedor;
import static br.unitins.tp1.Fixtures.marca;
import static br.unitins.tp1.Fixtures.material;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.dto.CapaceteAbertoDTO;
import br.unitins.tp1.dto.CapaceteAbertoResponseDTO;
import br.unitins.tp1.dto.EspecificacaoCapaceteDTO;
import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.service.CapaceteAbertoService;
import br.unitins.tp1.service.CategoriaService;
import br.unitins.tp1.service.CorService;
import br.unitins.tp1.service.FornecedorService;
import br.unitins.tp1.service.MarcaService;
import br.unitins.tp1.service.MaterialService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class CapaceteAbertoResourceTest {

    private final Marca marca = marca(1L, "LS2", "Espanha");
    private final Categoria categoria = categoria(2L, "Urbano", "Uso na cidade");
    private final Material material = material(3L, "Policarbonato", "Material leve");
    private final Cor cor = cor(4L, "Preto", "#000000");
    private final Fornecedor fornecedor1 = fornecedor(5L, "Moto Pecas LTDA", "11222333000181", "63992345678");
    private final Fornecedor fornecedor2 = fornecedor(6L, "Capacetes SA", "11444777000161", "6332145678");

    @Mock
    CapaceteAbertoService service;

    @Mock
    MarcaService marcaService;

    @Mock
    CategoriaService categoriaService;

    @Mock
    MaterialService materialService;

    @Mock
    CorService corService;

    @Mock
    FornecedorService fornecedorService;

    @InjectMocks
    CapaceteAbertoResource resource;

    @Captor
    ArgumentCaptor<CapaceteAberto> capaceteCaptor;

    @Test
    void listarRetorna200ComTodosOsCapacetes() {
        when(service.findAll()).thenReturn(List.of(capaceteAberto(1L, "Jet Classic"), capaceteAberto(2L, "Jet Sport")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        List<?> corpo = (List<?>) response.getEntity();
        assertEquals(2, corpo.size());
        assertEquals("Jet Classic", ((CapaceteAbertoResponseDTO) corpo.get(0)).modelo());
        assertEquals("Jet Sport", ((CapaceteAbertoResponseDTO) corpo.get(1)).modelo());
    }

    @Test
    void buscarPorIdRetorna200ComOCapacete() {
        when(service.findById(1L)).thenReturn(capaceteAberto(1L, "Jet Classic"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        CapaceteAbertoResponseDTO corpo = (CapaceteAbertoResponseDTO) response.getEntity();
        assertEquals(1L, corpo.id());
        assertEquals("Jet Classic", corpo.modelo());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Capacete nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorModeloRetorna200ComOsCapacetesEncontrados() {
        when(service.findByModelo("jet")).thenReturn(List.of(capaceteAberto(1L, "Jet Classic")));

        Response response = resource.buscarPorModelo("jet");

        assertEquals(200, response.getStatus());
        assertEquals(1, ((List<?>) response.getEntity()).size());
    }

    @Test
    void inserirMontaOCapaceteComAsReferenciasERetorna201() {
        stubReferencias();
        when(fornecedorService.findById(5L)).thenReturn(fornecedor1);
        when(fornecedorService.findById(6L)).thenReturn(fornecedor2);
        when(service.create(any(CapaceteAberto.class))).thenAnswer(invocation -> {
            CapaceteAberto capacete = invocation.getArgument(0);
            capacete.setId(10L);
            return capacete;
        });

        Response response = resource.inserir(dto(3, 2, List.of(5L, 6L)));

        verify(service).create(capaceteCaptor.capture());
        CapaceteAberto capacete = capaceteCaptor.getValue();
        assertEquals("Jet Classic", capacete.getModelo());
        assertEquals(new BigDecimal("899.90"), capacete.getPreco());
        assertEquals(Tamanho.M, capacete.getTamanho());
        assertEquals(10, capacete.getQuantidadeEstoque());
        assertSame(marca, capacete.getMarca());
        assertSame(categoria, capacete.getCategoria());
        assertSame(material, capacete.getMaterial());
        assertSame(cor, capacete.getCor());
        assertEquals(new BigDecimal("1.350"), capacete.getEspecificacao().getPeso());
        assertEquals(TipoFechamento.MICROMETRICO, capacete.getEspecificacao().getTipoFechamento());
        assertEquals(List.of(fornecedor1, fornecedor2), capacete.getFornecedores());
        assertTrue(capacete.getPossuiViseira());

        assertEquals(201, response.getStatus());
        CapaceteAbertoResponseDTO corpo = (CapaceteAbertoResponseDTO) response.getEntity();
        assertEquals(10L, corpo.id());
        assertEquals(2, corpo.fornecedores().size());
    }

    @Test
    void inserirSemFornecedoresGeraListaVazia() {
        stubReferencias();
        when(service.create(any(CapaceteAberto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        resource.inserir(dto(3, 2, null));

        verify(service).create(capaceteCaptor.capture());
        assertTrue(capaceteCaptor.getValue().getFornecedores().isEmpty());
        verifyNoInteractions(fornecedorService);
    }

    @Test
    void inserirComMarcaInexistentePropagaNotFoundSemCriar() {
        when(marcaService.findById(1L)).thenThrow(new NotFoundException("Marca nao encontrada."));

        assertThrows(NotFoundException.class, () -> resource.inserir(dto(3, 2, null)));

        verify(service, never()).create(any());
    }

    @Test
    void inserirComFornecedorInexistentePropagaNotFoundSemCriar() {
        stubReferencias();
        when(fornecedorService.findById(5L)).thenThrow(new NotFoundException("Fornecedor nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.inserir(dto(3, 2, List.of(5L))));

        verify(service, never()).create(any());
    }

    @Test
    void inserirComTamanhoInexistenteLancaBadRequest() {
        assertThrows(BadRequestException.class, () -> resource.inserir(dto(7, 2, null)));

        verifyNoInteractions(service);
    }

    @Test
    void inserirComTipoFechamentoInexistenteLancaBadRequest() {
        stubReferencias();

        assertThrows(BadRequestException.class, () -> resource.inserir(dto(3, 4, null)));

        verify(service, never()).create(any());
    }

    @Test
    void atualizarMontaOCapaceteERetorna204() {
        stubReferencias();

        Response response = resource.atualizar(1L, dto(5, 1, null));

        verify(service).update(eq(1L), capaceteCaptor.capture());
        assertEquals(Tamanho.XL, capaceteCaptor.getValue().getTamanho());
        assertEquals(TipoFechamento.DUPLO_D, capaceteCaptor.getValue().getEspecificacao().getTipoFechamento());
        assertSame(marca, capaceteCaptor.getValue().getMarca());
        assertEquals(204, response.getStatus());
    }

    @Test
    void excluirRetorna204() {
        Response response = resource.excluir(1L);

        verify(service).delete(1L);
        assertEquals(204, response.getStatus());
    }

    private void stubReferencias() {
        when(marcaService.findById(1L)).thenReturn(marca);
        when(categoriaService.findById(2L)).thenReturn(categoria);
        when(materialService.findById(3L)).thenReturn(material);
        when(corService.findById(4L)).thenReturn(cor);
    }

    private static CapaceteAbertoDTO dto(Integer idTamanho, Integer idTipoFechamento, List<Long> idFornecedores) {
        return new CapaceteAbertoDTO("Jet Classic", new BigDecimal("899.90"), idTamanho, 10, 1L, 2L, 3L, 4L,
                new EspecificacaoCapaceteDTO(new BigDecimal("1.350"), idTipoFechamento), idFornecedores, true);
    }
}
