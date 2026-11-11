package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.dto.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Livro;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LivroMapper {

    // Converte DTO de Request em entidade
    public Livro toEntity(LivroRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        Livro livro = new Livro();

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());

        // Autor e categorias serão associados no Service.

        return livro;
    }

    // Converte entidade em DTO de Response
    public LivroResponseDTO toResponseDTO(Livro livro) {

        if (livro == null) {
            return null;
        }

        Long autorId = livro.getAutor() != null
                ? livro.getAutor().getId()
                : null;

        String autorNome = livro.getAutor() != null
                ? livro.getAutor().getNome()
                : null;

        List<Long> categoriasIds = livro.getCategorias() != null
                ? livro.getCategorias().stream()
                    .map(categoria -> categoria.getId())
                    .toList()
                : Collections.emptyList();

        List<String> categoriasNomes = livro.getCategorias() != null
                ? livro.getCategorias().stream()
                    .map(categoria -> categoria.getNome())
                    .toList()
                : Collections.emptyList();

        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                autorId,
                autorNome,
                categoriasIds,
                categoriasNomes
        );
    }
}