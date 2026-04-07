package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DadosMeteorologicosService {
    private final DadosMeteorologicosRepository repository;

    public DadosMeteorologicos registrar(DadosMeteorologicosRequestDTO dto) {
        DadosMeteorologicos entity = new DadosMeteorologicos();
        entity.setCidade(dto.cidade());
        entity.setDataRegistro(dto.data());
        entity.setTempoDia(TempoEnum.valueOf(dto.tempoDia()));
        entity.setTempoNoite(TempoEnum.valueOf(dto.tempoNoite()));
        entity.setTemperaturaMaxima(dto.temperaturaMaxima());
        entity.setTemperaturaMinima(dto.temperaturaMinima());
        entity.setPrecipitacao(dto.precipitacao());
        entity.setUmidade(dto.umidade());
        entity.setVelocidadeVento(dto.velocidadeDoVento());
        return repository.save(entity);
    }
}
