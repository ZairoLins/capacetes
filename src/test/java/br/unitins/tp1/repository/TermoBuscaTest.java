package br.unitins.tp1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TermoBuscaTest {

    @Test
    void envolveOTermoComCuringas() {
        assertEquals("%ls2%", TermoBusca.contendo("ls2"));
    }

    @Test
    void escapaOPercentualDigitadoPeloUsuario() {
        assertEquals("%50!%%", TermoBusca.contendo("50%"));
    }

    @Test
    void escapaOSublinhadoDigitadoPeloUsuario() {
        assertEquals("%a!_b%", TermoBusca.contendo("a_b"));
    }

    @Test
    void escapaOProprioCaractereDeEscapeAntesDosDemais() {
        // "!" vira "!!" primeiro; senao o "!" inserido para "%" seria escapado de novo
        assertEquals("%!!!%%", TermoBusca.contendo("!%"));
    }

    @Test
    void termoVazioCasaComTudo() {
        assertEquals("%%", TermoBusca.contendo(""));
    }
}
