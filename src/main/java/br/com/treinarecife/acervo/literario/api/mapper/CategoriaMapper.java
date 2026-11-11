package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.dto.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CategoriaMapper {

    // Converte DTO de Request em entidade
    public Categoria toEntity(CategoriaRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setNome(dto.nome());

        return categoria;
    }

    // Converte entidade em DTO de Response
    public CategoriaResponseDTO toResponseDTO(Categoria categoria) {

        if (categoria == null) {
            return null;
        }

        List<Long> livrosIds = categoria.getLivros() != null
                ? categoria.getLivros().stream()
                    .map(livro -> livro.getId())
                    .toList()
                : Collections.emptyList();

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                livrosIds
        );
    }
}