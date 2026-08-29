package br.com.fiap.springmvc.controller;

import br.com.fiap.springmvc.model.Genero;
import br.com.fiap.springmvc.model.Livro;
import br.com.fiap.springmvc.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/livros")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("livro", new Livro());
        model.addAttribute("generos", Arrays.asList(Genero.values()));
        return "livroCadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Model model, @Valid Livro livro, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("livro", livro);
            model.addAttribute("generos", Arrays.asList(Genero.values()));
            return "livroCadastro";
        }
        livroService.create(livro);
        return lista(model);
    }

    @GetMapping("/lista")
    public String lista(Model model) {
        List<Livro> livros = livroService.readAll();
        model.addAttribute("listaLivros", livros);
        return "listaLivros";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable UUID id, Model model) {
        model.addAttribute("livro", livroService.readById(id));
        model.addAttribute("generos", Arrays.asList(Genero.values()));
        return "livroCadastro";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable UUID id, Model model) {
        boolean deletado = livroService.delete(id);
        if (deletado) {
            return lista(model);
        }
        return lista(model);
    }
}
