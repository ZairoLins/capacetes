package br.unitins.tp1.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.BadRequestException;

class CapaceteTest {

    @Test
    void atualizarEstoqueSomaAQuantidadeInformada() {
        CapaceteAberto capacete = new CapaceteAberto();
        capacete.setQuantidadeEstoque(10);

        capacete.atualizarEstoque(5);

        assertEquals(15, capacete.getQuantidadeEstoque());
    }

    @Test
    void atualizarEstoqueComValorNegativoDaBaixa() {
        CapaceteIntegral capacete = new CapaceteIntegral();
        capacete.setQuantidadeEstoque(10);

        capacete.atualizarEstoque(-10);

        assertEquals(0, capacete.getQuantidadeEstoque());
    }

    @Test
    void atualizarEstoqueConsideraEstoqueNuloComoZero() {
        CapaceteOffRoad capacete = new CapaceteOffRoad();

        capacete.atualizarEstoque(3);

        assertEquals(3, capacete.getQuantidadeEstoque());
    }

    @Test
    void atualizarEstoqueRejeitaSaldoNegativoSemAlterarOEstoque() {
        CapaceteAberto capacete = new CapaceteAberto();
        capacete.setQuantidadeEstoque(2);

        BadRequestException e = assertThrows(BadRequestException.class, () -> capacete.atualizarEstoque(-3));

        assertEquals("Estoque insuficiente.", e.getMessage());
        assertEquals(2, capacete.getQuantidadeEstoque());
    }
}
