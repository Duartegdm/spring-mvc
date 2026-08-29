package br.com.fiap.springmvc.service;

import br.com.fiap.springmvc.dto.LivroRequest;
import br.com.fiap.springmvc.model.Livro;
import br.com.fiap.springmvc.repository.LivroRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro create(Livro livro) {
//        Livro livro = new Livro();
//        BeanUtils.copyProperties(livroRequest, livro);
        return livroRepository.save(livro);
    }

    public Livro readById(UUID id) {
        return livroRepository.findById(id).orElse(null);
    }

    public List<Livro> readAll() {
        return livroRepository.findAll();
    }

    public Livro update(UUID id, LivroRequest livroRequest) {
        Optional<Livro> livroOptional = livroRepository.findById(id);
        if (livroOptional.isEmpty()) {
            return null;
        }
        Livro livroUpdate = livroOptional.get();
        BeanUtils.copyProperties(livroRequest, livroUpdate);
        livroUpdate.setId(id);
        return livroRepository.save(livroUpdate);
    }

    public boolean delete(UUID id) {
        livroRepository.deleteById(id);
        return readById(id) == null;
    }
}
