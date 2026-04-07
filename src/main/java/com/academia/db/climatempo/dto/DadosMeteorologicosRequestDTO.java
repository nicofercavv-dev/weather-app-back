package com.academia.db.climatempo.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosMeteorologicosRequestDTO(
        @NotBlank(message = "Nome da cidade é obrigatório")
        String cidade,

        @NotNull(message = "Data deve ser válida")
        LocalDate data,

        @NotNull(message = "Tempo dia deve ser válido")
        String tempoDia,

        @NotNull(message = "Tempo noite deve ser válido")
        String tempoNoite,

        @NotNull(message = "Temperatura máxima é obrigatória")
        Integer temperaturaMaxima,

        @NotNull(message = "Temperatura mínima é obrigatória")
        Integer temperaturaMinima,

        @NotNull(message = "Precipitação é obrigatória")
        @Max(value = 100, message = "Precipitação deve ser menor ou igual a 100ºC")
        @Min(value = 0, message = "Precipitação deve ser maior ou igual a 0ºC")
        Integer precipitacao,

        @NotNull(message = "Umidade é obrigatória")
        @Max(value = 100, message = "Umidade deve ser menor ou igual a 100ºC")
        @Min(value = 0, message = "Umidade deve ser maior ou igual a 0ºC")
        Integer umidade,

        @NotNull(message = "Velocidade do vento é obrigatória")
        @PositiveOrZero(message = "Velocidade do vento deve ser maior ou igual a 0km/h")
        Integer velocidadeDoVento
) {
}
