package br.com.fiap.springmvc.controller;

import br.com.fiap.springmvc.model.Genero;
import br.com.fiap.springmvc.model.Livro;
import br.com.fiap.springmvc.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// Teste de integracao do LivroController.
// Sobe a camada web, mas substitui o LivroService por um mock.
// Assim testamos o controller sem depender do banco de dados real.
@WebMvcTest(
        controllers = LivroController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class}
)
class LivroControllerTest {

    // MockMvc e usado para disparar requisicoes HTTP de teste
    @Autowired
    private MockMvc mockMvc;

    // @MockitoBean coloca um mock do LivroService no contexto do Spring
    @MockitoBean
    private LivroService livroService;

    @Test
    void listaDeveRetornarViewComLivros() throws Exception {
        // Arrange: configura o retorno do service
        List<Livro> livros = List.of(criarLivro(UUID.randomUUID()), criarLivro(UUID.randomUUID()));
        when(livroService.readAll()).thenReturn(livros);

        // Act & Assert: chama /livros/lista e verifica a resposta
        mockMvc.perform(get("/livros/lista"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaLivros"))
                .andExpect(model().attribute("listaLivros", livros));
    }

    @Test
    void cadastroDeveRetornarFormularioComGeneros() throws Exception {
        // Act & Assert: a pagina de cadastro deve trazer um livro vazio e a lista de generos
        mockMvc.perform(get("/livros/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("livroCadastro"))
                .andExpect(model().attributeExists("livro"))
                .andExpect(model().attribute("generos", Arrays.asList(Genero.values())));
    }

    @Test
    void cadastrarComDadosValidosDeveSalvarEListar() throws Exception {
        // Arrange: simula a lista que sera exibida apos o cadastro
        List<Livro> livros = List.of(criarLivro(UUID.randomUUID()));
        when(livroService.readAll()).thenReturn(livros);

        // Act & Assert: envia um POST com todos os dados validos
        mockMvc.perform(post("/livros/cadastrar")
                        .param("titulo", "O Hobbit")
                        .param("autor", "J.R.R. Tolkien")
                        .param("genero", "FICCAO_CIENTIFICA")
                        .param("paginas", "310")
                        .param("editora", "HarperCollins")
                        .param("isbn", "9701234567")
                        .param("dataPublicacao", "1937-09-21")
                        .param("preco", "39.90"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaLivros"))
                .andExpect(model().attribute("listaLivros", livros));

        // Verifica que o service create foi chamado com algum livro
        verify(livroService).create(any(Livro.class));
    }

    @Test
    void cadastrarComErroDeValidacaoDeveVoltarParaFormulario() throws Exception {
        // Act & Assert: envia POST com titulo em branco, o que viola @NotBlank
        mockMvc.perform(post("/livros/cadastrar")
                        .param("titulo", "") // em branco: viola @NotBlank
                        .param("autor", "J.R.R. Tolkien"))
                .andExpect(status().isOk())
                .andExpect(view().name("livroCadastro"))
                .andExpect(model().attributeExists("livro"))
                .andExpect(model().attributeExists("generos"));

        // Como houve erro, o metodo create NAO deve ser chamado
        verify(livroService, never()).create(any());
    }

    @Test
    void updateDeveCarregarLivroNoFormulario() throws Exception {
        // Arrange: simula que o livro sera encontrado pelo id
        UUID id = UUID.randomUUID();
        Livro livro = criarLivro(id);
        when(livroService.readById(id)).thenReturn(livro);

        // Act & Assert: acessa a tela de edicao
        mockMvc.perform(get("/livros/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("livroCadastro"))
                .andExpect(model().attribute("livro", livro))
                .andExpect(model().attribute("generos", Arrays.asList(Genero.values())));
    }

    @Test
    void deletarDeveRemoverERetornarParaLista() throws Exception {
        // Arrange: simula a remocao e uma lista vazia
        UUID id = UUID.randomUUID();
        List<Livro> livros = List.of();
        when(livroService.delete(id)).thenReturn(true);
        when(livroService.readAll()).thenReturn(livros);

        // Act & Assert
        mockMvc.perform(get("/livros/deletar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("listaLivros"))
                .andExpect(model().attribute("listaLivros", livros));

        // Verifica que o delete foi chamado com o id correto
        verify(livroService).delete(id);
    }

    // Metodo util para criar um livro de exemplo
    private Livro criarLivro(UUID id) {
        Livro livro = new Livro();
        livro.setId(id);
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
