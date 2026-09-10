package br.com.fiap.springmvc.service;

import br.com.fiap.springmvc.dto.LivroRequest;
import br.com.fiap.springmvc.model.Genero;
import br.com.fiap.springmvc.model.Livro;
import br.com.fiap.springmvc.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testes unitarios do LivroService.
// O objetivo e testar a logica do servico isoladamente.
// Para isso, usamos o Mockito para criar um "objeto falso" (mock)
// do LivroRepository, simulando o banco de dados.
@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    // @Mock cria uma implementacao falsa do repositorio
    @Mock
    private LivroRepository livroRepository;

    // @InjectMocks cria a instancia real do service e injeta o mock acima
    @InjectMocks
    private LivroService livroService;

    @Test
    void createDeveRetornarLivroSalvo() {
        // Arrange: cria um livro e define o que o mock deve retornar
        Livro livro = criarLivro(null);
        when(livroRepository.save(livro)).thenReturn(livro);

        // Act: chama o metodo que queremos testar
        Livro resultado = livroService.create(livro);

        // Assert: verifica o resultado
        assertNotNull(resultado);
        assertSame(livro, resultado);
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void readByIdDeveRetornarLivroQuandoEncontrado() {
        // Arrange
        UUID id = UUID.randomUUID();
        Livro livro = criarLivro(id);
        when(livroRepository.findById(id)).thenReturn(Optional.of(livro));

        // Act
        Livro resultado = livroService.readById(id);

        // Assert: agrupa varias verificacoes sobre o livro
        assertAll("Verificacoes do livro encontrado",
                () -> assertNotNull(resultado),
                () -> assertSame(livro, resultado),
                () -> assertEquals("O Senhor dos Anéis", resultado.getTitulo()),
                () -> assertEquals(Genero.FICCAO_CIENTIFICA, resultado.getGenero())
        );
    }

    @Test
    void readByIdDeveRetornarNuloQuandoNaoEncontrado() {
        // Arrange: simula que o repositorio nao encontrou o livro
        UUID id = UUID.randomUUID();
        when(livroRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Livro resultado = livroService.readById(id);

        // Assert
        assertNull(resultado);
        verify(livroRepository).findById(id);
    }

    @Test
    void readAllDeveRetornarListaDeLivros() {
        // Arrange
        Livro livro1 = criarLivro(UUID.randomUUID());
        Livro livro2 = criarLivro(UUID.randomUUID());
        List<Livro> livros = List.of(livro1, livro2);
        when(livroRepository.findAll()).thenReturn(livros);

        // Act
        List<Livro> resultado = livroService.readAll();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertIterableEquals(livros, resultado);
    }

    @Test
    void updateDeveAlterarLivroQuandoEncontrado() {
        // Arrange: livro ja existente e um LivroRequest com novos dados
        UUID id = UUID.randomUUID();
        Livro existente = criarLivro(id);
        LivroRequest request = new LivroRequest(
                "O Hobbit",
                "J.R.R. Tolkien",
                Genero.FICCAO_CIENTIFICA,
                310,
                "HarperCollins",
                "9701234567",
                LocalDate.of(1937, 9, 21),
                BigDecimal.valueOf(39.90)
        );

        when(livroRepository.findById(id)).thenReturn(Optional.of(existente));
        when(livroRepository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Livro resultado = livroService.update(id, request);

        // Assert
        assertAll("Verificacoes da atualizacao",
                () -> assertNotNull(resultado),
                () -> assertEquals(id, resultado.getId()),
                () -> assertEquals("O Hobbit", resultado.getTitulo()),
                () -> assertEquals("J.R.R. Tolkien", resultado.getAutor()),
                () -> assertEquals("HarperCollins", resultado.getEditora()),
                () -> assertNotSame(request, resultado)
        );
    }

    @Test
    void updateDeveRetornarNuloQuandoNaoEncontrado() {
        // Arrange
        UUID id = UUID.randomUUID();
        LivroRequest request = new LivroRequest(
                "O Hobbit",
                "J.R.R. Tolkien",
                Genero.FICCAO_CIENTIFICA,
                310,
                "HarperCollins",
                "9701234567",
                LocalDate.of(1937, 9, 21),
                BigDecimal.valueOf(39.90)
        );

        when(livroRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Livro resultado = livroService.update(id, request);

        // Assert
        assertNull(resultado);
        verify(livroRepository, never()).save(any());
    }

    @Test
    void deleteDeveRetornarTrueQuandoLivroNaoExisteMais() {
        // Arrange: simula que o livro foi deletado e nao e mais encontrado
        UUID id = UUID.randomUUID();

        doNothing().when(livroRepository).deleteById(id);
        when(livroRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean deletado = livroService.delete(id);

        // Assert
        assertTrue(deletado);
        verify(livroRepository).deleteById(id);
        verify(livroRepository, atLeastOnce()).findById(id);
    }

    @Test
    void deleteDeveRetornarFalseQuandoLivroAindaExiste() {
        // Arrange: simula que o delete nao funcionou e o livro ainda existe
        UUID id = UUID.randomUUID();
        Livro livro = criarLivro(id);

        doNothing().when(livroRepository).deleteById(id);
        when(livroRepository.findById(id)).thenReturn(Optional.of(livro));

        // Act
        boolean deletado = livroService.delete(id);

        // Assert
        assertFalse(deletado);
    }

    // Metodo util para montar um objeto Livro preenchido
    private Livro criarLivro(UUID id) {
        Livro livro = new Livro();
        if (id != null) {
            livro.setId(id);
        }
        livro.setTitulo("O Senhor dos Anéis");
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
