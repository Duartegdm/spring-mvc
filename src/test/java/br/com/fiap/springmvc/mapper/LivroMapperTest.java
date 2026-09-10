package br.com.fiap.springmvc.mapper;

import br.com.fiap.springmvc.dto.LivroRequest;
import br.com.fiap.springmvc.model.Genero;
import br.com.fiap.springmvc.model.Livro;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Testes unitarios do LivroMapper.
// Aqui testamos a conversao de um objeto Livro (entidade)
// para um objeto LivroRequest (DTO/record).
class LivroMapperTest {

    // Instanciamos o mapper diretamente, sem precisar do Spring
    private final LivroMapper mapper = new LivroMapper();

    @Test
    void livroToRequestDeveConverterTodosOsCampos() {
        // Arrange: preenche um livro com dados conhecidos
        Livro livro = new Livro();
        UUID id = UUID.randomUUID();
        livro.setId(id);
        livro.setTitulo("Duna");
        livro.setAutor("Frank Herbert");
        livro.setGenero(Genero.FICCAO_CIENTIFICA);
        livro.setPaginas(896);
        livro.setEditora("Aleph");
        livro.setIsbn("9701234567890");
        livro.setDataPublicacao(LocalDate.of(1965, 8, 1));
        livro.setPreco(BigDecimal.valueOf(59.90));

        // Act & Assert: verifica que a conversao nao lanca excecao
        assertDoesNotThrow(() -> mapper.LivroToRequest(livro));

        // Act: converte o livro para o DTO
        LivroRequest request = mapper.LivroToRequest(livro);

        // Assert: verifica cada campo convertido
        assertNotNull(request);
        assertNotSame(livro, request); // o DTO e um objeto diferente
        assertAll("Verificacoes do mapeamento",
                () -> assertEquals("Duna", request.titulo()),
                () -> assertEquals("Frank Herbert", request.autor()),
                () -> assertEquals(Genero.FICCAO_CIENTIFICA, request.genero()),
                () -> assertEquals(896, request.paginas()),
                () -> assertEquals("Aleph", request.editora()),
                () -> assertEquals("9701234567890", request.isbn()),
                () -> assertEquals(LocalDate.of(1965, 8, 1), request.dataPublicacao()),
                () -> assertEquals(BigDecimal.valueOf(59.90), request.preco())
        );
    }

    @Test
    void livroToRequestDeveLancarExcecaoComLivroNulo() {
        // Act & Assert: passar null deve lancar uma excecao
        Exception excecao = assertThrows(NullPointerException.class, () -> mapper.LivroToRequest(null));
        assertNotNull(excecao);
    }
}
