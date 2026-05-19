package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.exception.ResourceNotFoundException;
import com.academia.db.climatempo.mapper.DadosMeteorologicosMapper;
import com.academia.db.climatempo.exception.BusinessException;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DadosMeteorologicosService {
    private final DadosMeteorologicosRepository repository;
    private final DadosMeteorologicosMapper mapper;

    public DadosMeteorologicosResponseDTO registrar(DadosMeteorologicosRequestDTO dto) {
        DadosMeteorologicos entity = new DadosMeteorologicos();
        entity.setCidade(dto.cidade());
        entity.setDataRegistro(dto.data());
        entity.setTempoDia(converterTempo(dto.tempoDia(), "dia"));
        entity.setTempoNoite(converterTempo(dto.tempoNoite(), "noite"));
        entity.setTemperaturaMaxima(dto.temperaturaMaxima());
        entity.setTemperaturaMinima(dto.temperaturaMinima());
        entity.setPrecipitacao(dto.precipitacao());
        entity.setUmidade(dto.umidade());
        entity.setVelocidadeVento(dto.velocidadeDoVento());

        DadosMeteorologicos repositoryEntity = repository.save(entity);
        DadosMeteorologicosResponseDTO responseDTO = mapper.toResponseDTO(repositoryEntity);

        return responseDTO;
    }

    public Page<DadosMeteorologicosResponseDTO> listar(String cidade, Pageable pageable) {
        Page<DadosMeteorologicos> entidades;

        if (cidade != null && !cidade.isBlank()) {
            entidades = repository.findByCidadeContainingIgnoreCase(cidade, pageable);
        } else {
            entidades = repository.findAll(pageable);
        }

        return entidades.map(mapper::toResponseDTO);
    }

    public Optional<DadosMeteorologicos> buscarPorId(Long id) {
        Optional<DadosMeteorologicos> entidade;
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dado de ID " + id + " não existe.");
        }

        entidade = repository.findById(id);

        return entidade;
    }

    public List<DadosMeteorologicosResponseDTO> listarProximosSeteDias(String cidade) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(7);

        List<DadosMeteorologicos> entidades = repository.findByCidadeContainingIgnoreCaseAndDataRegistroBetweenOrderByDataRegistroAsc(
                cidade,
                hoje,
                limite
        );

        return entidades.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public DadosMeteorologicosResponseDTO editar(Long id, DadosMeteorologicosRequestDTO dto) {
        DadosMeteorologicos entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado com o ID: " + id));

        entity.setCidade(dto.cidade());
        entity.setDataRegistro(dto.data());
        entity.setTempoDia(converterTempo(dto.tempoDia(), "dia"));
        entity.setTempoNoite(converterTempo(dto.tempoNoite(), "noite"));
        entity.setTemperaturaMaxima(dto.temperaturaMaxima());
        entity.setTemperaturaMinima(dto.temperaturaMinima());
        entity.setPrecipitacao(dto.precipitacao());
        entity.setUmidade(dto.umidade());
        entity.setVelocidadeVento(dto.velocidadeDoVento());

        DadosMeteorologicos atualizado = repository.save(entity);
        return mapper.toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Não é possível deletar: ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    private TempoEnum converterTempo(String valor, String periodo) {
        try {
            return TempoEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Condição climática de " + periodo + " inválida: " + valor);
        }
    }
}
