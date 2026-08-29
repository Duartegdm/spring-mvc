package br.com.fiap.springmvc.dto;

import br.com.fiap.springmvc.model.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LivroRequest(
        @NotBlank(message = "O título é obrigatório") String titulo,
        @NotBlank(message = "O autor é obrigatório") String autor,
        @NotNull(message = "O gênero é obrigatório") Genero genero,
        Integer paginas,
        String editora,
        @Pattern(regexp = "^970\\d{7}$|^970\\d{10}$") String isbn,
        LocalDate dataPublicacao,
        BigDecimal preco
) {

}
