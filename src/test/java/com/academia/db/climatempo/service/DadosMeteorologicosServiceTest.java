package com.academia.db.climatempo.service;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.exception.ResourceNotFoundException;
import com.academia.db.climatempo.mapper.DadosMeteorologicosMapper;
import com.academia.db.climatempo.exception.BusinessException;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("Deve lançar BusinessException quando enum não existir")
    void deveLancarErroQuandoEnumNaoExistir() {
        LocalDate localDateMock = LocalDate.of(2026,04,07);
        var dto = new DadosMeteorologicosRequestDTO("Cidade Teste", localDateMock, "NEVE_COM_SOL", "CHUVA", 40, 28, 10, 50, 4);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.registrar(dto);
        });

        assertEquals("Condição climática de dia inválida: NEVE_COM_SOL", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar todos os registros quando cidade for nula")
    void deveRetornarTodosQuandoCidadeForNula() {
        Pageable pageable = PageRequest.of(0, 10);
        var entidade = new DadosMeteorologicos();
        entidade.setCidade("São Paulo");

        var responseDTO = DadosMeteorologicosResponseDTO.builder().id(34L).cidade("São Paulo").data(LocalDate.now()).tempoDia("SOL").tempoNoite("LIMPO").temperaturaMaxima(30).temperaturaMinima(20).precipitacao(0).umidade(50).velocidadeDoVento(10).build();

        Page<DadosMeteorologicos> pageEntidades = new PageImpl<>(List.of(entidade));

        when(repository.findAll(pageable)).thenReturn(pageEntidades);
        when(mapper.toResponseDTO(entidade)).thenReturn(responseDTO);

        var resultado = service.listar(null, pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("São Paulo", resultado.getContent().get(0).cidade());
        verify(repository, times(1)).findAll(pageable);
        verify(repository, never()).findByCidadeContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar todos os registros quando cidade for vazia")
    void deveRetornarTodosQuandoCidadeForVazia() {
        Pageable pageable = PageRequest.of(0, 10);
        var entidade = new DadosMeteorologicos();
        entidade.setCidade("São Paulo");

        var responseDTO = DadosMeteorologicosResponseDTO.builder().id(34L).cidade("São Paulo").data(LocalDate.now()).tempoDia("SOL").tempoNoite("LIMPO").temperaturaMaxima(30).temperaturaMinima(20).precipitacao(0).umidade(50).velocidadeDoVento(10).build();

        Page<DadosMeteorologicos> pageEntidades = new PageImpl<>(List.of(entidade));
        when(repository.findAll(pageable)).thenReturn(pageEntidades);
        when(mapper.toResponseDTO(entidade)).thenReturn(responseDTO);

        var resultado = service.listar("", pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("São Paulo", resultado.getContent().get(0).cidade());
        verify(repository, times(1)).findAll(pageable);
        verify(repository, never()).findByCidadeContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve filtrar por cidade quando o parâmetro for informado")
    void deveFiltrarPorCidade() {
        String cidadeBusca = "Açailândia";
        Pageable pageable = PageRequest.of(0, 10);
        var entidade = new DadosMeteorologicos();
        entidade.setCidade(cidadeBusca);

        var responseDTO = DadosMeteorologicosResponseDTO.builder().id(34L).cidade(cidadeBusca).data(LocalDate.now()).tempoDia("SOL").tempoNoite("LIMPO").temperaturaMaxima(30).temperaturaMinima(20).precipitacao(0).umidade(50).velocidadeDoVento(10).build();

        Page<DadosMeteorologicos> pageEntidades = new PageImpl<>(List.of(entidade));
        when(repository.findByCidadeContainingIgnoreCase(cidadeBusca, pageable)).thenReturn(pageEntidades);
        when(mapper.toResponseDTO(entidade)).thenReturn(responseDTO);

        var resultado = service.listar(cidadeBusca, pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(cidadeBusca, resultado.getContent().get(0).cidade());
        verify(repository, times(1)).findByCidadeContainingIgnoreCase(cidadeBusca, pageable);
        verify(repository, never()).findAll(pageable);
    }

    @Test
    @DisplayName("Deve listar dados dos próximos 7 dias ordenados por data")
    void deveListarProximosSeteDiasComSucesso() {
        String cidade = "Açailândia";
        var entidade1 = new DadosMeteorologicos();
        entidade1.setDataRegistro(LocalDate.now());

        var entidade2 = new DadosMeteorologicos();
        entidade2.setDataRegistro(LocalDate.now().plusDays(1));

        var dto1 = DadosMeteorologicosResponseDTO.builder().cidade(cidade).data(LocalDate.now()).build();
        var dto2 = DadosMeteorologicosResponseDTO.builder().cidade(cidade).data(LocalDate.now().plusDays(1)).build();

        when(repository.findByCidadeContainingIgnoreCaseAndDataRegistroBetweenOrderByDataRegistroAsc(
                eq(cidade),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(List.of(entidade1, entidade2));

        when(mapper.toResponseDTO(entidade1)).thenReturn(dto1);
        when(mapper.toResponseDTO(entidade2)).thenReturn(dto2);

        List<DadosMeteorologicosResponseDTO> resultado = service.listarProximosSeteDias(cidade);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(LocalDate.now(), resultado.get(0).data());
        assertEquals(LocalDate.now().plusDays(1), resultado.get(1).data());

        verify(repository, times(1))
                .findByCidadeContainingIgnoreCaseAndDataRegistroBetweenOrderByDataRegistroAsc(eq(cidade), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Deve atualizar dados meteorológicos com sucesso")
    void deveAtualizarDadosComSucesso() {
        Long id = 1L;
        var request = new DadosMeteorologicosRequestDTO("Açailândia", LocalDate.now(), "SOL", "LIMPO", 35, 20, 0, 50, 10);
        var entidadeExistente = new DadosMeteorologicos();
        entidadeExistente.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entidadeExistente));
        when(repository.save(any(DadosMeteorologicos.class))).thenReturn(entidadeExistente);
        when(mapper.toResponseDTO(any())).thenReturn(new DadosMeteorologicosResponseDTO(1L,"Açailândia", LocalDate.now(), "SOL", "LIMPO", 35, 20, 0, 50, 10));

        var resultado = service.editar(id, request);

        assertNotNull(resultado);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar editar ID inexistente")
    void deveLancarErroAoEditarInexistente() {
        Long idInexistente = 99L;
        var request = new DadosMeteorologicosRequestDTO(
                "Açailândia", LocalDate.now(), "SOL", "LIMPO", 35, 20, 0, 50, 10
        );

        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.editar(idInexistente, request);
        });

        assertEquals("Registro não encontrado com o ID: " + idInexistente, exception.getMessage());

        verify(repository, never()).save(any(DadosMeteorologicos.class));
    }

    @Test
    @DisplayName("Deve deletar dado meteorológico com sucesso")
    void deveDeletarComSucesso() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.deletar(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar ID inexistente")
    void deveLancarErroAoDeletarInexistente() {
        Long id = 99L;
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deletar(id));
    }
}