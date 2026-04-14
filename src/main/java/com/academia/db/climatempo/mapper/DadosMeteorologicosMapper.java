package com.academia.db.climatempo.mapper;

import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import org.springframework.stereotype.Component;

@Component
public class DadosMeteorologicosMapper {

    public DadosMeteorologicosResponseDTO toResponseDTO(DadosMeteorologicos entidade) {
        return new DadosMeteorologicosResponseDTO(
                entidade.getCidade(),
                entidade.getDataRegistro(),
                entidade.getTempoDia().name(),
                entidade.getTempoNoite().name(),
                entidade.getTemperaturaMaxima(),
                entidade.getTemperaturaMinima(),
                entidade.getPrecipitacao(),
                entidade.getUmidade(),
                entidade.getVelocidadeVento()
        );
    }
}