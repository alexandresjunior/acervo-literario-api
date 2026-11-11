
package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AutorMapper {

    // Converte DTO de Request em entidade
    public Autor toEntity(AutorRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        Autor autor = new Autor();

        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());

        return autor;
    }

    // Converte entidade em DTO de Response
    public AutorResponseDTO toResponseDTO(Autor autor) {

        if (autor == null) {
            return null;
        }

        Long biografiaId = autor.getBiografia() != null
                ? autor.getBiografia().getId()
                : null;

        List<Long> livrosIds = autor.getLivros() != null
                ? autor.getLivros().stream()
                    .map(livro -> livro.getId())
                    .toList()
                : Collections.emptyList();

        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getNacionalidade(),
                biografiaId,
                livrosIds
        );
    }
}
