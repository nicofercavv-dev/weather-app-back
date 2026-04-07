package com.academia.db.climatempo.controller;

import com.academia.db.climatempo.dto.DadosMeteorologicosRequestDTO;
import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.service.DadosMeteorologicosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dados-meteorologicos")
@RequiredArgsConstructor
@Tag(name = "Dados Meteorológicos",description = "Endpoints para gestão de dados meteorológicos")
public class DadosMeteorologicosController {
    private final DadosMeteorologicosService service;

    @PostMapping
    @Operation(summary = "Cadastrar novos dados")
    public ResponseEntity<DadosMeteorologicos> cadastrar(@RequestBody @Valid DadosMeteorologicosRequestDTO dto) {
        var resultado = service.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}
