package com.academia.db.climatempo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "dados_meteorologicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosMeteorologicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cidade;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "tempo_dia", nullable = false)
    @Enumerated(EnumType.STRING)
    private TempoEnum tempoDia;

    @Column(name = "tempo_noite", nullable = false)
    @Enumerated(EnumType.STRING)
    private TempoEnum tempoNoite;

    @Column(name = "temperatura_max", nullable = false)
    private Integer temperaturaMaxima;

    @Column(name = "temperatura_min", nullable = false)
    private Integer temperaturaMinima;

    @Column(nullable = false)
    private Integer precipitacao;

    @Column(nullable = false)
    private Integer umidade;

    @Column(name = "velocidade_vento", nullable = false)
    private Integer velocidadeVento;
}
