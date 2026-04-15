package com.academia.db.climatempo.integration;

import com.academia.db.climatempo.model.DadosMeteorologicos;
import com.academia.db.climatempo.model.TempoEnum;
import com.academia.db.climatempo.repository.DadosMeteorologicosRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
public class DatabaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DadosMeteorologicosRepository dadosMeteorologicosRepository;

    @Test
    void connectionEstablishedTest() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void devePersistirDadosMeteorologicos() {
        DadosMeteorologicos entity = DadosMeteorologicos.builder().cidade("Rio de Janeiro").dataRegistro(LocalDate.now()).tempoDia(TempoEnum.SOL).tempoNoite(TempoEnum.CHUVA).temperaturaMaxima(37).temperaturaMinima(23).precipitacao(80).umidade(78).velocidadeVento(12).build();
        DadosMeteorologicos salvo = dadosMeteorologicosRepository.save(entity);

        assertThat(salvo.getId()).isNotNull();

        Optional<DadosMeteorologicos> buscado = dadosMeteorologicosRepository.findById(salvo.getId());
        assertThat(buscado).isPresent();
        assertThat(buscado.get().getCidade()).isEqualTo("Rio de Janeiro");
        assertThat(buscado.get().getTempoDia()).isEqualTo(TempoEnum.SOL);
    }
}
