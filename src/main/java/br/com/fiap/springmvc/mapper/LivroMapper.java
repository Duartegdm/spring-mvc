package br.com.fiap.springmvc.mapper;

import br.com.fiap.springmvc.dto.LivroRequest;
import br.com.fiap.springmvc.model.Livro;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {
    public LivroRequest LivroToRequest(Livro livro) {
        return new LivroRequest(livro.getTitulo(),
                livro.getAutor(),
                livro.getGenero(),
                livro.getPaginas(),
                livro.getEditora(),
                livro.getIsbn(),
                livro.getDataPublicacao(),
                livro.getPreco());
    }
}
