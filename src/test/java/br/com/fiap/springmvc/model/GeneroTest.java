package br.com.fiap.springmvc.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Testes simples do enum Genero.
// O foco aqui e praticar alguns tipos de assercao do JUnit.
class GeneroTest {

    @Test
    void generoDeveTerDescricaoCorreta() {
        // Verifica se a descricao do genero esta correta
        assertEquals("Terror", Genero.TERROR.getDescricao());

        // Verifica que Terror NAO e descrito como Romance
        assertNotEquals("Romance", Genero.TERROR.getDescricao());

        // Verifica que a descricao contem o texto esperado
        assertTrue(Genero.FICCAO_CIENTIFICA.getDescricao().contains("Científica"));
    }

    @Test
    void valuesDeveConterTodosOsGeneros() {
        // Arrange: lista esperada com todos os generos na ordem declarada
        Genero[] esperados = {
                Genero.TERROR,
                Genero.ROMANCE,
                Genero.ACAO,
                Genero.SUSPENSE,
                Genero.FICCAO_CIENTIFICA,
                Genero.BIOGRAFIA,
                Genero.FILOSOFIA,
                Genero.RELIGIOSO,
                Genero.HISTORIA,
                Genero.CIENCIA,
                Genero.DIDATICO
        };

        // Assert: compara os arrays (mesmo conteudo, na mesma ordem)
        assertArrayEquals(esperados, Genero.values());
    }
}
