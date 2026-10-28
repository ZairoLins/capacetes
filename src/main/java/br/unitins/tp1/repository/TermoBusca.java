package br.unitins.tp1.repository;

/**
 * Monta o padrao das buscas "contem" (LIKE). Os caracteres % e _ digitados
 * pelo usuario sao escapados com '!', para serem buscados como texto e nao
 * como coringa. As consultas devem usar: LIKE ... ESCAPE '!'
 */
final class TermoBusca {

    private TermoBusca() {
    }

    static String contendo(String termo) {
        String escapado = termo.replace("!", "!!").replace("%", "!%").replace("_", "!_");
        return "%" + escapado + "%";
    }
}
