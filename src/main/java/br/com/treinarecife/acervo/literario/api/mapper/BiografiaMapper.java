package br.com.treinarecife.acervo.literario.api.mapper;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;

import org.springframework.stereotype.Component;

@Component
public class BiografiaMapper {

    // Converte DTO de Request em entidade
    public Biografia toEntity(BiografiaRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        Biografia biografia = new Biografia();

        biografia.setTexto(dto.texto());
        biografia.setDataNascimento(dto.dataNascimento());

        // O relacionamento com Autor será definido no Service.

        return biografia;
    }

    // Converte entidade em DTO de Response
    public BiografiaResponseDTO toResponseDTO(Biografia biografia) {

        if (biografia == null) {
            return null;
        }

        Long autorId = biografia.getAutor() != null
                ? biografia.getAutor().getId()
                : null;

        return new BiografiaResponseDTO(
                biografia.getId(),
                biografia.getTexto(),
                biografia.getDataNascimento(),
                autorId
        );
    }
}