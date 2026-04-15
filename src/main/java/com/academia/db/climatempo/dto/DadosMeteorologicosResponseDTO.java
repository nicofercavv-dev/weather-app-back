package com.academia.db.climatempo.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DadosMeteorologicosResponseDTO(
        Long id,
        String cidade,
        LocalDate data,
        String tempoDia,
        String tempoNoite,
        Integer temperaturaMaxima,
        Integer temperaturaMinima,
        Integer precipitacao,
        Integer umidade,
        Integer velocidadeDoVento
) {
}