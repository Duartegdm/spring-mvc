package br.com.fiap.springmvc.repository;

import br.com.fiap.springmvc.model.Genero;
import br.com.fiap.springmvc.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Teste de integracao com @DataJpaTest.
// Ele sobe uma parte do contexto Spring ligada ao JPA,
// usando o banco H2 em memoria (configurado automaticamente).
@DataJpaTest
class LivroRepositoryTest {

    // O Spring injeta o repositorio real, conectado ao H2
    @Autowired
    private LivroRepository livroRepository;

    @Test
    void saveDevePersistirLivro() {
        // Arrange
        Livro livro = criarLivro("O Senhor dos Anéis");

        // Act
        Livro salvo = livroRepository.save(livro);

        // Assert: o banco gerou um id automaticamente
        assertNotNull(salvo.getId());
    }

    @Test
    void findByIdDeveRetornarLivroSalvo() {
        // Arrange
        Livro livro = criarLivro("O Hobbit");
        Livro salvo = livroRepository.save(livro);

        // Act
        Optional<Livro> resultado = livroRepository.findById(salvo.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("O Hobbit", resultado.get().getTitulo());
    }

    @Test
    void findAllDeveRetornarTodosOsLivros() {
        // Arrange
        livroRepository.save(criarLivro("O Senhor dos Anéis"));
        livroRepository.save(criarLivro("O Hobbit"));

        // Act
        List<Livro> livros = livroRepository.findAll();

        // Assert
        assertEquals(2, livros.size());
    }

    @Test
    void deleteByIdDeveRemoverLivro() {
        // Arrange
        Livro salvo = livroRepository.save(criarLivro("Duna"));

        // Act
        livroRepository.deleteById(salvo.getId());

        // Assert
        Optional<Livro> resultado = livroRepository.findById(salvo.getId());
        assertFalse(resultado.isPresent());
    }

    // Metodo auxiliar para criar livros com titulos diferentes
    private Livro criarLivro(String titulo) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor("J.R.R. Tolkien");
        livro.setGenero(Genero.FICCAO_CIENTIFICA);
        livro.setPaginas(1200);
        livro.setEditora("HarperCollins");
        livro.setIsbn("9701234567");
        livro.setDataPublicacao(LocalDate.of(1954, 7, 29));
        livro.setPreco(BigDecimal.valueOf(89.90));
        return livro;
    }
}
