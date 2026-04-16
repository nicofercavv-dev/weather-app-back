package com.academia.db.climatempo.exception;

import com.academia.db.climatempo.controller.DadosMeteorologicosController;
import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.service.DadosMeteorologicosService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DadosMeteorologicosController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DadosMeteorologicosService service;

    @Test
    @DisplayName("Deve tratar ResourceNotFoundException e retornar 404")
    void deveTratarResourceNotFound() throws Exception {
        when(service.listar(anyString())).thenThrow(new ResourceNotFoundException("Registro não encontrado"));

        mockMvc.perform(get("/dados-meteorologicos")
                        .param("cidade", "Qualquer"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Registro não encontrado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve retornar 422 quando o service lançar BusinessException por Enum inválido")
    void deveRetornar422AoRegistrarComEnumInvalido() throws Exception {
        String mensagemErro = "Condição climática de dia inválida: CHUVA_NEVE";

        when(service.registrar(any(DadosMeteorologicosRequestDTO.class)))
                .thenThrow(new BusinessException(mensagemErro));

        mockMvc.perform(post("/dados-meteorologicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "cidade": "Açailândia",
                            "data": "2026-04-16",
                            "tempoDia": "CHUVA_NEVE",
                            "tempoNoite": "SOL",
                            "temperaturaMaxima": "40",
                            "temperaturaMinima": "24",
                            "precipitacao": "30",
                            "umidade": "50",
                            "velocidadeDoVento": "10"
                        }
                    """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve tratar exceções genéricas e retornar 500")
    void deveTratarExceptionGenerica() throws Exception {
        when(service.listar(null)).thenThrow(new RuntimeException("Erro interno imprevisto"));

        mockMvc.perform(get("/dados-meteorologicos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno inesperado no servidor. Tente novamente mais tarde."));
    }

    @Test
    @DisplayName("Deve tratar erro de parâmetro ausente e retornar 400")
    void deveTratarMissingServletRequestParameterException() throws Exception {
        mockMvc.perform(get("/dados-meteorologicos/previsao"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("cidade")));
    }
}