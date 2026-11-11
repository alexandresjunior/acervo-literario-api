package br.com.treinarecife.acervo.literario.api.dto.response;

import java.util.List;

public record AutorResponseDTO(
    Long id,
    String nome,
    String nacionalidade,
    Long biografiaId,
    List<Long> livrosIds
) {}