package br.com.treinarecife.acervo.literario.api.dto.response;

import java.util.List;

public record CategoriaResponseDTO(
    Long id,
    String nome,
    List<Long> livrosIds
) {}