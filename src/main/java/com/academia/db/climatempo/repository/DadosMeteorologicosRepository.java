package com.academia.db.climatempo.repository;

import com.academia.db.climatempo.model.DadosMeteorologicos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DadosMeteorologicosRepository extends JpaRepository<DadosMeteorologicos, Long> {
    Page<DadosMeteorologicos> findByCidadeContainingIgnoreCase(String cidade, Pageable pageable);
    List<DadosMeteorologicos> findByCidadeContainingIgnoreCaseAndDataRegistroBetweenOrderByDataRegistroAsc(
            String cidade,
            LocalDate dataInicio,
            LocalDate dataFim
    );
}
