package com.academia.db.climatempo.dto;

import java.time.LocalDate;

public record DadosMeteorologicosResponseDTO(
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
