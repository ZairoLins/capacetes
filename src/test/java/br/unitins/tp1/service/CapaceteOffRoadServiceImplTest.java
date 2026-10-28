package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.capaceteOffRoad;
import static br.unitins.tp1.Fixtures.categoria;
import static br.unitins.tp1.Fixtures.cor;
import static br.unitins.tp1.Fixtures.especificacao;
import static br.unitins.tp1.Fixtures.fornecedor;
import static br.unitins.tp1.Fixtures.marca;
import static br.unitins.tp1.Fixtures.material;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.repository.CapaceteOffRoadRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CapaceteOffRoadServiceImplTest {

    @Mock
    CapaceteOffRoadRepository repository;

    @InjectMocks
    CapaceteOffRoadServiceImpl service;

    @Test
    void createPersisteERetornaOCapacete() {
        CapaceteOffRoad capacete = capaceteOffRoad(null, "MX436 Pioneer");

        CapaceteOffRoad criado = service.create(capacete);

        verify(repository).persist(capacete);
        assertSame(capacete, criado);
    }

    @Test
    void updateCopiaTodosOsCamposParaOCapaceteExistente() {
        CapaceteOffRoad existente = capaceteOffRoad(1L, "MX436 Pioneer");
        EspecificacaoCapacete especificacaoOriginal = existente.getEspecificacao();
        when(repository.findById(1L)).thenReturn(existente);

        CapaceteOffRoad dados = new CapaceteOffRoad();
        dados.setModelo("MX700 Subverter");
        dados.setPreco(new BigDecimal("2100.00"));
        dados.setTamanho(Tamanho.L);
        dados.setQuantidadeEstoque(0);
        dados.setMarca(marca(2L, "Shoei", "Japao"));
        dados.setCategoria(categoria(2L, "Trilha", "Uso fora de estrada"));
        dados.setMaterial(material(2L, "Fibra", "Fibra de vidro"));
        dados.setCor(cor(2L, "Laranja", "#FF8800"));
        dados.setEspecificacao(especificacao("1.250", TipoFechamento.DUPLO_D));
        List<Fornecedor> fornecedores = List.of(fornecedor(2L, "Capacetes SA", "11444777000161", "6332145678"));
        dados.setFornecedores(fornecedores);
        dados.setPossuiPala(false);

        service.update(1L, dados);

        assertEquals(1L, existente.getId());
        assertEquals("MX700 Subverter", existente.getModelo());
        assertEquals(new BigDecimal("2100.00"), existente.getPreco());
        assertEquals(Tamanho.L, existente.getTamanho());
        assertEquals(0, existente.getQuantidadeEstoque());
        assertSame(dados.getMarca(), existente.getMarca());
        assertSame(dados.getCategoria(), existente.getCategoria());
        assertSame(dados.getMaterial(), existente.getMaterial());
        assertSame(dados.getCor(), existente.getCor());
        assertSame(fornecedores, existente.getFornecedores());
        assertFalse(existente.getPossuiPala());

        // composicao: a especificacao existente e mantida e apenas seus campos mudam
        assertSame(especificacaoOriginal, existente.getEspecificacao());
        assertEquals(new BigDecimal("1.250"), especificacaoOriginal.getPeso());
        assertEquals(TipoFechamento.DUPLO_D, especificacaoOriginal.getTipoFechamento());
    }

    @Test
    void updateLancaNotFoundQuandoOCapaceteNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, capaceteOffRoad(null, "MX700 Subverter")));
        assertEquals("Capacete nao encontrado.", e.getMessage());
    }

    @Test
    void deleteRemoveOCapaceteExistente() {
        when(repository.deleteById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteLancaNotFoundQuandoOCapaceteNaoExiste() {
        when(repository.deleteById(99L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.delete(99L));
        assertEquals("Capacete nao encontrado.", e.getMessage());
    }

    @Test
    void findByIdRetornaOCapacete() {
        CapaceteOffRoad capacete = capaceteOffRoad(1L, "MX436 Pioneer");
        when(repository.findById(1L)).thenReturn(capacete);

        assertSame(capacete, service.findById(1L));
    }

    @Test
    void findByIdLancaNotFoundQuandoOCapaceteNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.findById(99L));
        assertEquals("Capacete nao encontrado.", e.getMessage());
    }

    @Test
    void findByModeloRetornaOResultadoDoRepositorio() {
        List<CapaceteOffRoad> capacetes = List.of(capaceteOffRoad(1L, "MX436 Pioneer"));
        when(repository.findByModelo("mx")).thenReturn(capacetes);

        assertSame(capacetes, service.findByModelo("mx"));
    }

    @Test
    void findAllRetornaTodosOsCapacetes() {
        List<CapaceteOffRoad> capacetes = List.of(capaceteOffRoad(1L, "MX436 Pioneer"));
        when(repository.listAll()).thenReturn(capacetes);

        assertSame(capacetes, service.findAll());
    }
}
