package br.com.treinarecife.acervo.literario.api.dto.request;

import java.time.LocalDate;

public record BiografiaRequestDTO(
    String texto,
    LocalDate dataNascimento,
    Long autorId
) {}