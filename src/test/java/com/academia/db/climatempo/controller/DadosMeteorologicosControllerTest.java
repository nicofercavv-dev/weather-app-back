package com.academia.db.climatempo.controller;

import com.academia.db.climatempo.service.DadosMeteorologicosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DadosMeteorologicosController.class)
class DadosMeteorologicosControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}