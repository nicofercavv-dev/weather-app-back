package com.academia.db.climatempo.controller;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.service.DadosMeteorologicosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        var response = DadosMeteorologicosResponseDTO.builder().id(23L).cidade("Açailândia").data(LocalDate.now()).tempoDia("SOL").tempoNoite("LIMPO").temperaturaMaxima(35).temperaturaMinima(20).precipitacao(0).umidade(40).velocidadeDoVento(15).build();

        when(service.listar("Açailândia")).thenReturn(List.of(response));

        mockMvc.perform(get("/dados-meteorologicos")
                        .param("cidade", "Açailândia")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cidade").value("Açailândia"))
                .andExpect(jsonPath("$[0].temperaturaMaxima").value(35));
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
                "SOL",
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
}