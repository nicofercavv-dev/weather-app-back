package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.exception.BusinessException;
import com.academia.db.climatempo.mapper.DadosMeteorologicosMapper;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DadosMeteorologicosServiceTest {

    @Mock
    private DadosMeteorologicosRepository repository;

    @Mock
    private DadosMeteorologicosMapper mapper;

    @InjectMocks
    private DadosMeteorologicosService service;

    @Test
    @DisplayName("Deve salvar os dados meteorológicos com sucesso")
    void deveSalvarComSucesso() {
        var dto = new DadosMeteorologicosRequestDTO(
                "Açailândia", LocalDate.of(2026, 4, 7), "SOL", "CHUVA", 40, 10, 5, 30, 20
        );

        service.registrar(dto);

        verify(repository, times(1)).save(any(DadosMeteorologicos.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o tempo informado for inválido")
    void deveLancarErroAoInformarTempoInvalido() {
        DadosMeteorologicosRequestDTO dto = DadosMeteorologicosRequestDTO.builder().cidade("Açailândia").data(LocalDate.now()).tempoDia("VALOR_INEXISTENTE").tempoNoite("CHUVA").temperaturaMaxima(28).temperaturaMinima(19).precipitacao(80).umidade(80).velocidadeDoVento(13).build();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.registrar(dto);
        });

        assertTrue(exception.getMessage().contains("Condição climática de dia inválida"));

        verify(repository, never()).save(any());
    }
}