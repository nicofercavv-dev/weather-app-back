package com.academia.db.climatempo.controller;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.service.DadosMeteorologicosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DadosMeteorologicosController.class)
class DadosMeteorologicosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @MockitoBean
    private DadosMeteorologicosService service;

    @Test
    void deveRetornar400QuandoCidadeForVazia() throws Exception {
        String jsonInput = """
                {
                    "cidade": "",
                    "data": "2026-04-07",
                    "precipitacao": 150
                }
                """;

        mockMvc.perform(post("/dados-meteorologicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao listar dados meteorológicos")
    void deveRetornarStatus200AoListar() throws Exception {
        var response = DadosMeteorologicosResponseDTO.builder().id(23L).cidade("Açailândia").data(LocalDate.now()).tempoDia("CHUVA").tempoNoite("LIMPO").temperaturaMaxima(35).temperaturaMinima(20).precipitacao(70).umidade(40).velocidadeDoVento(15).build();

        Page<DadosMeteorologicosResponseDTO> page = new PageImpl<>(List.of(response));

        when(service.listar(eq("Açailândia"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/dados-meteorologicos")
                        .param("cidade", "Açailândia")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cidade").value("Açailândia"))
                .andExpect(jsonPath("$.content[0].temperaturaMaxima").value(35))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando houver erro de validação no cadastro")
    void deveRetornar400AoCadastrarInvalido() throws Exception {
        String jsonInvalido = "{}";

        mockMvc.perform(post("/dados-meteorologicos")
                        .content(jsonInvalido)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro de validação, há campos inválidos"));
    }

    @Test
    @DisplayName("Deve retornar 201 Created e o objeto salvo ao cadastrar com sucesso")
    void deveRetornar21AoCadastrarComSucesso() throws Exception {
        var requestDTO = new DadosMeteorologicosRequestDTO(
                "Açailândia",
                LocalDate.of(2026, 4, 10),
                "NUBLADO",
                "LIMPO",
                35, 22, 0, 50, 10
        );

        var entidadeSalva = DadosMeteorologicosResponseDTO.builder().id(1L).cidade("Açailândia").build();

        when(service.registrar(any(DadosMeteorologicosRequestDTO.class))).thenReturn(entidadeSalva);

        mockMvc.perform(post("/dados-meteorologicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cidade").value("Açailândia"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar previsão de 7 dias")
    void deveRetornarPrevisaoSeteDias() throws Exception {
        String cidadeBusca = "Açailândia";
        var previsao = DadosMeteorologicosResponseDTO.builder().cidade(cidadeBusca).data(LocalDate.now().plusDays(1)).tempoDia("LIMPO").tempoNoite("LIMPO").temperaturaMaxima(34).temperaturaMinima(24).precipitacao(0).umidade(45).velocidadeDoVento(12).build();

        when(service.listarProximosSeteDias(cidadeBusca)).thenReturn(List.of(previsao));

        mockMvc.perform(get("/dados-meteorologicos/previsao")
                        .param("cidade", cidadeBusca)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cidade").value(cidadeBusca))
                .andExpect(jsonPath("$[0].data").value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o parâmetro cidade não for enviado na previsão")
    void deveRetornar400QuandoCidadeAusente() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/previsao")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao editar registro")
    void deveRetornar200AoEditar() throws Exception {
        Long id = 1L;
        var dto = new DadosMeteorologicosRequestDTO("Açailândia", LocalDate.now(), "CHUVA", "LIMPO", 35, 20, 80, 50, 10);

        when(service.editar(eq(id), any())).thenReturn(new DadosMeteorologicosResponseDTO(1L,"Açailândia", LocalDate.now(), "CHUVA", "LIMPO", 35, 20, 80, 50, 10));

        mockMvc.perform(put("/dados-meteorologicos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 204 ao deletar registro")
    void deveRetornar204AoDeletar() throws Exception {
        Long id = 1L;

        doNothing().when(service).deletar(id);

        mockMvc.perform(delete("/dados-meteorologicos/{id}", id))
                .andExpect(status().isNoContent());
    }
}