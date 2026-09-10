package br.com.fiap.springmvc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Teste de integracao basico: sobe o contexto completo do Spring Boot.
// Este teste apenas verifica se a aplicacao consegue inicializar sem erros.
//
// IMPORTANTE: rodar esta classe so executa o metodo contextLoads().
// Para rodar TODOS os testes do projeto, use:
//   - terminal: ./gradlew test
//   - IntelliJ: clique direito em src/test/java -> Run 'All Tests'
//   - VS Code: abra a aba Testing e clique no botao "Run Tests" do projeto
@SpringBootTest
class SpringMvcApplicationTests {

    @Test
    void contextLoads() {
        // Se o contexto subir, este teste passa.
    }

}
