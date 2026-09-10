package br.com.fiap.springmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// Teste de integracao da camada web (controller).
// @WebMvcTest sobe apenas o contexto MVC, sem subir o servidor real.
// O excludeAutoConfiguration desativa a seguranca so para este teste,
// facilitando o exemplo com os alunos.
@WebMvcTest(
        controllers = HelloController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class}
)
class HelloControllerTest {

    // MockMvc simula requisicoes HTTP para o controller
    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloDeveRetornarViewHelloComMensagem() throws Exception {
        // Perform: faz uma requisicao GET para /hello
        mockMvc.perform(get("/hello"))
                // andExpect: verifica a resposta da requisicao
                .andExpect(status().isOk())                       // status 200
                .andExpect(view().name("hello"))                  // nome da view Thymeleaf
                .andExpect(model().attribute("message", "Hello World!")); // atributo no Model
    }
}
