package br.unitins.tp1.service;

import static br.unitins.tp1.Fixtures.capaceteIntegral;
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

import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.EspecificacaoCapacete;
import br.unitins.tp1.model.Fornecedor;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;
import br.unitins.tp1.repository.CapaceteIntegralRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CapaceteIntegralServiceImplTest {

    @Mock
    CapaceteIntegralRepository repository;

    @InjectMocks
    CapaceteIntegralServiceImpl service;

    @Test
    void createPersisteERetornaOCapacete() {
        CapaceteIntegral capacete = capaceteIntegral(null, "FF353 Rapid");

        CapaceteIntegral criado = service.create(capacete);

        verify(repository).persist(capacete);
        assertSame(capacete, criado);
    }

    @Test
    void updateCopiaTodosOsCamposParaOCapaceteExistente() {
        CapaceteIntegral existente = capaceteIntegral(1L, "FF353 Rapid");
        EspecificacaoCapacete especificacaoOriginal = existente.getEspecificacao();
        when(repository.findById(1L)).thenReturn(existente);

        CapaceteIntegral dados = new CapaceteIntegral();
        dados.setModelo("FF800 Storm");
        dados.setPreco(new BigDecimal("1500.00"));
        dados.setTamanho(Tamanho.S);
        dados.setQuantidadeEstoque(7);
        dados.setMarca(marca(2L, "Shoei", "Japao"));
        dados.setCategoria(categoria(2L, "Esportivo", "Uso em pista"));
        dados.setMaterial(material(2L, "Fibra", "Fibra de vidro"));
        dados.setCor(cor(2L, "Branco", "#FFFFFF"));
        dados.setEspecificacao(especificacao("1.600", TipoFechamento.ENGATE_RAPIDO));
        List<Fornecedor> fornecedores = List.of(fornecedor(2L, "Capacetes SA", "11444777000161", "6332145678"));
        dados.setFornecedores(fornecedores);
        dados.setPossuiViseiraSolar(false);

        service.update(1L, dados);

        assertEquals(1L, existente.getId());
        assertEquals("FF800 Storm", existente.getModelo());
        assertEquals(new BigDecimal("1500.00"), existente.getPreco());
        assertEquals(Tamanho.S, existente.getTamanho());
        assertEquals(7, existente.getQuantidadeEstoque());
        assertSame(dados.getMarca(), existente.getMarca());
        assertSame(dados.getCategoria(), existente.getCategoria());
        assertSame(dados.getMaterial(), existente.getMaterial());
        assertSame(dados.getCor(), existente.getCor());
        assertSame(fornecedores, existente.getFornecedores());
        assertFalse(existente.getPossuiViseiraSolar());

        // composicao: a especificacao existente e mantida e apenas seus campos mudam
        assertSame(especificacaoOriginal, existente.getEspecificacao());
        assertEquals(new BigDecimal("1.600"), especificacaoOriginal.getPeso());
        assertEquals(TipoFechamento.ENGATE_RAPIDO, especificacaoOriginal.getTipoFechamento());
    }

    @Test
    void updateLancaNotFoundQuandoOCapaceteNaoExiste() {
        when(repository.findById(99L)).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> service.update(99L, capaceteIntegral(null, "FF800 Storm")));
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
        CapaceteIntegral capacete = capaceteIntegral(1L, "FF353 Rapid");
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
        List<CapaceteIntegral> capacetes = List.of(capaceteIntegral(1L, "FF353 Rapid"));
        when(repository.findByModelo("rapid")).thenReturn(capacetes);

        assertSame(capacetes, service.findByModelo("rapid"));
    }

    @Test
    void findAllRetornaTodosOsCapacetes() {
        List<CapaceteIntegral> capacetes = List.of(capaceteIntegral(1L, "FF353 Rapid"));
        when(repository.listAll()).thenReturn(capacetes);

        assertSame(capacetes, service.findAll());
    }
}
