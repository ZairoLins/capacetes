package br.unitins.tp1.resource;

import static br.unitins.tp1.Fixtures.capaceteOffRoad;
import static br.unitins.tp1.Fixtures.categoria;
import static br.unitins.tp1.Fixtures.cor;
import static br.unitins.tp1.Fixtures.fornecedor;
import static br.unitins.tp1.Fixtures.marca;
import static br.unitins.tp1.Fixtures.material;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import br.unitins.tp1.dto.CapaceteOffRoadDTO;
import br.unitins.tp1.dto.CapaceteOffRoadResponseDTO;
import br.unitins.tp1.dto.EspecificacaoCapaceteDTO;
import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.Categoria;
import br.unitins.tp1.model.Cor;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Marca;
import br.unitins.tp1.model.Material;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.service.CapaceteOffRoadService;
import br.unitins.tp1.service.CategoriaService;
import br.unitins.tp1.service.CorService;
import br.unitins.tp1.service.FornecedorService;
import br.unitins.tp1.service.MarcaService;
import br.unitins.tp1.service.MaterialService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class CapaceteOffRoadResourceTest {

    private final Marca marca = marca(1L, "LS2", "Espanha");
    private final Categoria categoria = categoria(2L, "Trilha", "Uso fora de estrada");
    private final Material material = material(3L, "Fibra de vidro", "Material resistente");
    private final Cor cor = cor(4L, "Laranja", "#FF8800");
    private final Fornecedor fornecedor = fornecedor(5L, "Moto Pecas LTDA", "11222333000181", "63992345678");

    @Mock
    CapaceteOffRoadService service;

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
    CapaceteOffRoadResource resource;

    @Captor
    ArgumentCaptor<CapaceteOffRoad> capaceteCaptor;

    @Test
    void listarRetorna200ComTodosOsCapacetes() {
        when(service.findAll()).thenReturn(List.of(capaceteOffRoad(1L, "MX436 Pioneer")));

        Response response = resource.listar();

        assertEquals(200, response.getStatus());
        List<?> corpo = (List<?>) response.getEntity();
        assertEquals(1, corpo.size());
        assertEquals("MX436 Pioneer", ((CapaceteOffRoadResponseDTO) corpo.get(0)).modelo());
    }

    @Test
    void buscarPorIdRetorna200ComOCapacete() {
        when(service.findById(1L)).thenReturn(capaceteOffRoad(1L, "MX436 Pioneer"));

        Response response = resource.buscarPorId(1L);

        assertEquals(200, response.getStatus());
        CapaceteOffRoadResponseDTO corpo = (CapaceteOffRoadResponseDTO) response.getEntity();
        assertEquals(1L, corpo.id());
        assertTrue(corpo.possuiPala());
    }

    @Test
    void buscarPorIdPropagaNotFound() {
        when(service.findById(99L)).thenThrow(new NotFoundException("Capacete nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.buscarPorId(99L));
    }

    @Test
    void buscarPorModeloRetorna200ComOsCapacetesEncontrados() {
        when(service.findByModelo("mx")).thenReturn(List.of(capaceteOffRoad(1L, "MX436 Pioneer")));

        Response response = resource.buscarPorModelo("mx");

        assertEquals(200, response.getStatus());
        assertEquals(1, ((List<?>) response.getEntity()).size());
    }

    @Test
    void inserirMontaOCapaceteComAsReferenciasERetorna201() {
        stubReferencias();
        when(fornecedorService.findById(5L)).thenReturn(fornecedor);
        when(service.create(any(CapaceteOffRoad.class))).thenAnswer(invocation -> {
            CapaceteOffRoad capacete = invocation.getArgument(0);
            capacete.setId(10L);
            return capacete;
        });

        Response response = resource.inserir(dto(4, 1, List.of(5L), false));

        verify(service).create(capaceteCaptor.capture());
        CapaceteOffRoad capacete = capaceteCaptor.getValue();
        assertEquals("MX436 Pioneer", capacete.getModelo());
        assertEquals(new BigDecimal("1099.90"), capacete.getPreco());
        assertEquals(Tamanho.L, capacete.getTamanho());
        assertEquals(2, capacete.getQuantidadeEstoque());
        assertSame(marca, capacete.getMarca());
        assertSame(categoria, capacete.getCategoria());
        assertSame(material, capacete.getMaterial());
        assertSame(cor, capacete.getCor());
        assertEquals(new BigDecimal("1.200"), capacete.getEspecificacao().getPeso());
        assertEquals(TipoFechamento.DUPLO_D, capacete.getEspecificacao().getTipoFechamento());
        assertEquals(List.of(fornecedor), capacete.getFornecedores());
        assertFalse(capacete.getPossuiPala());

        assertEquals(201, response.getStatus());
        assertEquals(10L, ((CapaceteOffRoadResponseDTO) response.getEntity()).id());
    }

    @Test
    void inserirSemFornecedoresGeraListaVazia() {
        stubReferencias();
        when(service.create(any(CapaceteOffRoad.class))).thenAnswer(invocation -> invocation.getArgument(0));

        resource.inserir(dto(3, 2, null, true));

        verify(service).create(capaceteCaptor.capture());
        assertTrue(capaceteCaptor.getValue().getFornecedores().isEmpty());
        verifyNoInteractions(fornecedorService);
    }

    @Test
    void inserirComMaterialInexistentePropagaNotFoundSemCriar() {
        when(marcaService.findById(1L)).thenReturn(marca);
        when(categoriaService.findById(2L)).thenReturn(categoria);
        when(materialService.findById(3L)).thenThrow(new NotFoundException("Material nao encontrado."));

        assertThrows(NotFoundException.class, () -> resource.inserir(dto(3, 2, null, true)));

        verify(service, never()).create(any());
    }

    @Test
    void inserirComTipoFechamentoInexistenteLancaBadRequest() {
        stubReferencias();

        assertThrows(BadRequestException.class, () -> resource.inserir(dto(3, 0, null, true)));

        verify(service, never()).create(any());
    }

    @Test
    void atualizarMontaOCapaceteERetorna204() {
        stubReferencias();

        Response response = resource.atualizar(1L, dto(2, 3, null, true));

        verify(service).update(eq(1L), capaceteCaptor.capture());
        assertEquals(Tamanho.S, capaceteCaptor.getValue().getTamanho());
        assertEquals(TipoFechamento.ENGATE_RAPIDO, capaceteCaptor.getValue().getEspecificacao().getTipoFechamento());
        assertTrue(capaceteCaptor.getValue().getPossuiPala());
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

    private static CapaceteOffRoadDTO dto(Integer idTamanho, Integer idTipoFechamento, List<Long> idFornecedores,
            Boolean possuiPala) {
        return new CapaceteOffRoadDTO("MX436 Pioneer", new BigDecimal("1099.90"), idTamanho, 2, 1L, 2L, 3L, 4L,
                new EspecificacaoCapaceteDTO(new BigDecimal("1.200"), idTipoFechamento), idFornecedores, possuiPala);
    }
}
