package com.academia.db.climatempo.repository;

import com.academia.db.climatempo.model.DadosMeteorologicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosMeteorologicosRepository extends JpaRepository<DadosMeteorologicos, Long> {
    List<DadosMeteorologicos> findByCidadeContainingIgnoreCase(String cidade);
}
