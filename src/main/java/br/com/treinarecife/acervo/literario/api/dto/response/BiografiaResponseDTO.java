package br.com.treinarecife.acervo.literario.api.dto.response;

import java.time.LocalDate;

public record BiografiaResponseDTO(
    Long id,
    String texto,
    LocalDate dataNascimento,
    Long autorId
) {}