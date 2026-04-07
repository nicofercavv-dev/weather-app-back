package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.exception.BusinessException;
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
    @DisplayName("Deve lançar BusinessException quando enum não existir")
    void deveLancarErroAoDuplicarRegistro() {
        LocalDate localDateMock = LocalDate.of(2026,04,07);
        var dto = new DadosMeteorologicosRequestDTO("Cidade Teste", localDateMock, "NEVE_COM_SOL", "CHUVA", 40, 28, 10, 50, 4);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.registrar(dto);
        });

        assertEquals("No enum constant com.academia.db.climatempo.model.TempoEnum.NEVE_COM_SOL", exception.getMessage());

        verify(repository, never()).save(any());
    }
}