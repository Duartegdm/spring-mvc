package br.com.fiap.springmvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/livros/lista").permitAll()
                                        .requestMatchers("/livros/cadastro").permitAll()
                                        .requestMatchers("/livros/cadastrar").permitAll()
                                        .requestMatchers("/livros/update").permitAll()
                                        .requestMatchers("/livros/deletar").permitAll()
                                        .anyRequest().authenticated()
                ).oauth2Login(oauth2 ->
                        oauth2
                                .defaultSuccessUrl("/livros/lista")
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
