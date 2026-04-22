package com.academia.db.climatempo.mapper;

import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DadosMeteorologicosMapperTest {
    private DadosMeteorologicosMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DadosMeteorologicosMapper();
    }

    @Test
    @DisplayName("Deve converter entidade para DTO corretamente")
    void deveConverterEntidadeParaResponseDTO() {
        DadosMeteorologicos entidade = new DadosMeteorologicos();
        entidade.setId(1L);
        entidade.setCidade("Açailândia");
        entidade.setDataRegistro(LocalDate.of(2026, 4, 20));
        entidade.setTempoDia(TempoEnum.SOL);
        entidade.setTempoNoite(TempoEnum.LIMPO);
        entidade.setTemperaturaMaxima(32);
        entidade.setTemperaturaMinima(23);
        entidade.setPrecipitacao(10);
        entidade.setUmidade(60);
        entidade.setVelocidadeVento(15);

        DadosMeteorologicosResponseDTO dto = mapper.toResponseDTO(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.id());
        assertEquals(entidade.getCidade(), dto.cidade());
        assertEquals(entidade.getDataRegistro(), dto.data());
        assertEquals("SOL", dto.tempoDia());
        assertEquals("LIMPO", dto.tempoNoite());
        assertEquals(entidade.getTemperaturaMaxima(), dto.temperaturaMaxima());
        assertEquals(entidade.getTemperaturaMinima(), dto.temperaturaMinima());
        assertEquals(entidade.getPrecipitacao(), dto.precipitacao());
        assertEquals(entidade.getUmidade(), dto.umidade());
        assertEquals(entidade.getVelocidadeVento(), dto.velocidadeDoVento());
    }
}
