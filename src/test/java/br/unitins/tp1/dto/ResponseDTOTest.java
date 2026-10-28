package br.unitins.tp1.dto;

import static br.unitins.tp1.Fixtures.capaceteAberto;
import static br.unitins.tp1.Fixtures.capaceteIntegral;
import static br.unitins.tp1.Fixtures.capaceteOffRoad;
import static br.unitins.tp1.Fixtures.categoria;
import static br.unitins.tp1.Fixtures.cor;
import static br.unitins.tp1.Fixtures.especificacao;
import static br.unitins.tp1.Fixtures.estado;
import static br.unitins.tp1.Fixtures.fornecedor;
import static br.unitins.tp1.Fixtures.marca;
import static br.unitins.tp1.Fixtures.material;
import static br.unitins.tp1.Fixtures.municipio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.tp1.model.CapaceteAberto;
import br.unitins.tp1.model.CapaceteIntegral;
import br.unitins.tp1.model.CapaceteOffRoad;
import br.unitins.tp1.model.Estado;
import br.unitins.tp1.model.Regiao;
import br.unitins.tp1.model.Tamanho;
import br.unitins.tp1.model.TipoFechamento;

class ResponseDTOTest {

    @Test
    void corFromEntity() {
        assertEquals(new CorResponseDTO(1L, "Preto", "#000000"), CorResponseDTO.fromEntity(cor(1L, "Preto", "#000000")));
    }

    @Test
    void categoriaFromEntity() {
        assertEquals(new CategoriaResponseDTO(1L, "Urbano", "Uso na cidade"),
                CategoriaResponseDTO.fromEntity(categoria(1L, "Urbano", "Uso na cidade")));
    }

    @Test
    void marcaFromEntity() {
        assertEquals(new MarcaResponseDTO(1L, "LS2", "Espanha"), MarcaResponseDTO.fromEntity(marca(1L, "LS2", "Espanha")));
    }

    @Test
    void materialFromEntity() {
        assertEquals(new MaterialResponseDTO(1L, "Policarbonato", "Material leve"),
                MaterialResponseDTO.fromEntity(material(1L, "Policarbonato", "Material leve")));
    }

    @Test
    void estadoFromEntity() {
        assertEquals(new EstadoResponseDTO(1L, "Tocantins", "TO", Regiao.NORTE),
                EstadoResponseDTO.fromEntity(estado(1L, "Tocantins", "TO", Regiao.NORTE)));
    }

    @Test
    void municipioFromEntityIncluiOEstado() {
        Estado estado = estado(1L, "Tocantins", "TO", Regiao.NORTE);

        assertEquals(new MunicipioResponseDTO(7L, "Palmas", new EstadoResponseDTO(1L, "Tocantins", "TO", Regiao.NORTE)),
                MunicipioResponseDTO.fromEntity(municipio(7L, "Palmas", estado)));
    }

    @Test
    void fornecedorFromEntity() {
        assertEquals(new FornecedorResponseDTO(1L, "Moto Pecas LTDA", "11222333000181", "63992345678"),
                FornecedorResponseDTO.fromEntity(fornecedor(1L, "Moto Pecas LTDA", "11222333000181", "63992345678")));
    }

    @Test
    void especificacaoFromEntity() {
        assertEquals(new EspecificacaoCapaceteResponseDTO(new BigDecimal("1.350"), TipoFechamento.MICROMETRICO),
                EspecificacaoCapaceteResponseDTO.fromEntity(especificacao("1.350", TipoFechamento.MICROMETRICO)));
    }

    @Test
    void capaceteAbertoFromEntityMapeiaTodosOsCampos() {
        CapaceteAberto capacete = capaceteAberto(1L, "Jet Classic");

        CapaceteAbertoResponseDTO dto = CapaceteAbertoResponseDTO.fromEntity(capacete);

        assertEquals(1L, dto.id());
        assertEquals("Jet Classic", dto.modelo());
        assertEquals(new BigDecimal("899.90"), dto.preco());
        assertEquals(Tamanho.M, dto.tamanho());
        assertEquals(10, dto.quantidadeEstoque());
        assertEquals(MarcaResponseDTO.fromEntity(capacete.getMarca()), dto.marca());
        assertEquals(CategoriaResponseDTO.fromEntity(capacete.getCategoria()), dto.categoria());
        assertEquals(MaterialResponseDTO.fromEntity(capacete.getMaterial()), dto.material());
        assertEquals(CorResponseDTO.fromEntity(capacete.getCor()), dto.cor());
        assertEquals(new EspecificacaoCapaceteResponseDTO(new BigDecimal("1.350"), TipoFechamento.MICROMETRICO),
                dto.especificacao());
        assertEquals(List.of(FornecedorResponseDTO.fromEntity(capacete.getFornecedores().get(0))), dto.fornecedores());
        assertTrue(dto.possuiViseira());
    }

    @Test
    void capaceteAbertoSemFornecedoresGeraListaVazia() {
        CapaceteAberto capacete = capaceteAberto(1L, "Jet Classic");
        capacete.setFornecedores(null);

        assertEquals(List.of(), CapaceteAbertoResponseDTO.fromEntity(capacete).fornecedores());
    }

    @Test
    void capaceteIntegralFromEntityMapeiaOCampoEspecifico() {
        CapaceteIntegral capacete = capaceteIntegral(2L, "FF353 Rapid");
        capacete.setPossuiViseiraSolar(false);

        CapaceteIntegralResponseDTO dto = CapaceteIntegralResponseDTO.fromEntity(capacete);

        assertEquals(2L, dto.id());
        assertEquals("FF353 Rapid", dto.modelo());
        assertEquals(1, dto.fornecedores().size());
        assertFalse(dto.possuiViseiraSolar());
    }

    @Test
    void capaceteIntegralSemFornecedoresGeraListaVazia() {
        CapaceteIntegral capacete = capaceteIntegral(2L, "FF353 Rapid");
        capacete.setFornecedores(null);

        assertEquals(List.of(), CapaceteIntegralResponseDTO.fromEntity(capacete).fornecedores());
    }

    @Test
    void capaceteOffRoadFromEntityMapeiaOCampoEspecifico() {
        CapaceteOffRoad capacete = capaceteOffRoad(3L, "MX436 Pioneer");

        CapaceteOffRoadResponseDTO dto = CapaceteOffRoadResponseDTO.fromEntity(capacete);

        assertEquals(3L, dto.id());
        assertEquals("MX436 Pioneer", dto.modelo());
        assertEquals(Tamanho.M, dto.tamanho());
        assertTrue(dto.possuiPala());
    }

    @Test
    void capaceteOffRoadSemFornecedoresGeraListaVazia() {
        CapaceteOffRoad capacete = capaceteOffRoad(3L, "MX436 Pioneer");
        capacete.setFornecedores(null);

        assertEquals(List.of(), CapaceteOffRoadResponseDTO.fromEntity(capacete).fornecedores());
    }
}
