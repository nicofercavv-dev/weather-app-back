package com.academia.db.climatempo.controller;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.dto.DadosMeteorologicosResponseDTO;
import com.academia.db.climatempo.service.DadosMeteorologicosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dados-meteorologicos")
@RequiredArgsConstructor
@Tag(name = "Dados Meteorológicos",description = "Endpoints para gestão de dados meteorológicos")
public class DadosMeteorologicosController {
    private final DadosMeteorologicosService service;

    @PostMapping
    @Operation(summary = "Cadastrar novos dados")
    public ResponseEntity<DadosMeteorologicosResponseDTO> cadastrar(@RequestBody @Valid DadosMeteorologicosRequestDTO dto) {
        var resultado = service.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping
    @Operation(summary = "Listar dados meteorológicos")
    public ResponseEntity<List<DadosMeteorologicosResponseDTO>> listar(@RequestParam(value = "cidade", required = false) String cidade) {
        List<DadosMeteorologicosResponseDTO> resultado = service.listar(cidade);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }

    @GetMapping("/previsao")
    @Operation(summary = "Listar dados meteorológicos de uma cidade para os próximos 7 dias")
    public ResponseEntity<List<DadosMeteorologicosResponseDTO>> listarProximosDias(
            @RequestParam(value = "cidade") String cidade) {

        List<DadosMeteorologicosResponseDTO> resultado = service.listarProximosSeteDias(cidade);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
}
