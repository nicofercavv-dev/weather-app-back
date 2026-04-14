package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.mapper.DadosMeteorologicosMapper;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DadosMeteorologicosService {
    private final DadosMeteorologicosRepository repository;
    private final DadosMeteorologicosMapper mapper;

    public DadosMeteorologicosResponseDTO registrar(DadosMeteorologicosRequestDTO dto) {
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

        DadosMeteorologicos repositoryEntity = repository.save(entity);
        DadosMeteorologicosResponseDTO responseDTO = mapper.toResponseDTO(repositoryEntity);

        return responseDTO;
    }
}
