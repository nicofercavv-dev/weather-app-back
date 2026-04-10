package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.mapper.DadosMeteorologicosMapper;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    @DisplayName("Deve retornar todos os registros quando cidade for nula")
    void deveRetornarTodosQuandoCidadeForNula() {
        var entidade = new DadosMeteorologicos();
        entidade.setCidade("São Paulo");

        var responseDTO = new DadosMeteorologicosResponseDTO(
                "São Paulo", LocalDate.now(), "SOL", "LIMPO", 30, 20, 0, 50, 10
        );

        when(repository.findAll()).thenReturn(List.of(entidade));
        when(mapper.toResponseDTO(entidade)).thenReturn(responseDTO);

        var resultado = service.listar(null);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("São Paulo", resultado.get(0).cidade());
        verify(repository, times(1)).findAll();
        verify(repository, never()).findByCidadeContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Deve filtrar por cidade quando o parâmetro for informado")
    void deveFiltrarPorCidade() {
        String cidadeBusca = "Açailândia";
        var entidade = new DadosMeteorologicos();
        entidade.setCidade(cidadeBusca);

        var responseDTO = new DadosMeteorologicosResponseDTO(
                cidadeBusca, LocalDate.now(), "SOL", "LIMPO", 30, 20, 0, 50, 10
        );

        when(repository.findByCidadeContainingIgnoreCase(cidadeBusca)).thenReturn(List.of(entidade));
        when(mapper.toResponseDTO(entidade)).thenReturn(responseDTO);

        var resultado = service.listar(cidadeBusca);

        assertEquals(1, resultado.size());
        assertEquals(cidadeBusca, resultado.get(0).cidade());
        verify(repository, times(1)).findByCidadeContainingIgnoreCase(cidadeBusca);
        verify(repository, never()).findAll();
    }
}