package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import py.edu.uc.lp3.domain.Arma;
import py.edu.uc.lp3.domain.ArmaDeFuego;
import py.edu.uc.lp3.domain.Fusil;
import py.edu.uc.lp3.domain.Humo;
import py.edu.uc.lp3.domain.Pistola;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;
import py.edu.uc.lp3.repository.ArmaRepository;
import py.edu.uc.lp3.rest.dto.EscenarioCombateRequest;
import py.edu.uc.lp3.rest.dto.EscenarioCombateResponse;
import py.edu.uc.lp3.rest.dto.ExplosionA2Request;
import py.edu.uc.lp3.rest.dto.RafagaA3Request;
import py.edu.uc.lp3.rest.dto.SimulacionRequest;

@SpringBootTest
class EscenarioCombateServiceTest {

    @Autowired
    private ArmaRepository armaRepository;

    @Autowired
    private ArmaService armaService;

    @BeforeEach
    void limpiarBase() {
        armaRepository.deleteAll();
    }

    @Test
    void escenarioValidoValidaA2YA3AntesDePersistirLaSimulacionLocal() {
        SimulacionRequest simulacion = crearArmas();
        List<String> orden = new ArrayList<>();
        CompaneroA2Client a2 = request -> {
            orden.add("A2");
            return Map.of("granadaId", 13, "simulacion", true);
        };
        CompaneroA3Client a3 = request -> {
            orden.add("A3");
            return Map.of("estadoFinal", "30/87");
        };
        EscenarioCombateService servicio = new EscenarioCombateService(a2, a3, armaService);

        EscenarioCombateResponse respuesta = servicio.ejecutar(escenario(simulacion));

        assertThat(orden).containsExactly("A2", "A3");
        assertThat(respuesta.validacionA2()).containsEntry("simulacion", true);
        assertThat(respuesta.validacionA3()).containsEntry("estadoFinal", "30/87");
        assertThat(respuesta.simulacionLocal().atacanteMunicionDespues()).isEqualTo(29);
        assertThat(((ArmaDeFuego) armaRepository.findById(simulacion.atacanteId()).orElseThrow()).getMunicionActual())
                .isEqualTo(29);
    }

    @Test
    void rechazoDeA2EvitaLlamarAA3YNoPersiste() {
        SimulacionRequest simulacion = crearArmas();
        AtomicInteger llamadasA3 = new AtomicInteger();
        CompaneroA2Client a2 = request -> {
            throw new ValidacionCompaneroRechazadaException("A2", 400, "solo explotan granadas HE");
        };
        CompaneroA3Client a3 = request -> {
            llamadasA3.incrementAndGet();
            return Map.of();
        };
        EscenarioCombateService servicio = new EscenarioCombateService(a2, a3, armaService);

        assertThatThrownBy(() -> servicio.ejecutar(escenario(simulacion)))
                .isInstanceOf(ValidacionCompaneroRechazadaException.class)
                .hasMessageContaining("A2");
        assertThat(llamadasA3.get()).isZero();
        assertThat(armaRepository.count()).isEqualTo(3);
        assertThat(((ArmaDeFuego) armaRepository.findById(simulacion.atacanteId()).orElseThrow()).getMunicionActual())
                .isEqualTo(30);
    }

    @Test
    void rechazoDeA3NoPersisteLaSimulacionLocal() {
        SimulacionRequest simulacion = crearArmas();
        CompaneroA2Client a2 = request -> Map.of("simulacion", true);
        CompaneroA3Client a3 = request -> {
            throw new ValidacionCompaneroRechazadaException("A3", 400, "veces debe estar entre 1 y 100");
        };
        EscenarioCombateService servicio = new EscenarioCombateService(a2, a3, armaService);

        assertThatThrownBy(() -> servicio.ejecutar(escenario(simulacion)))
                .isInstanceOf(ValidacionCompaneroRechazadaException.class)
                .hasMessageContaining("A3");
        assertThat(armaRepository.count()).isEqualTo(3);
        assertThat(((ArmaDeFuego) armaRepository.findById(simulacion.atacanteId()).orElseThrow()).getMunicionActual())
                .isEqualTo(30);
    }

    private SimulacionRequest crearArmas() {
        Arma atacante = armaService.create(new Fusil("AK-47", 0, 2700, 36, 71, 2.4, 600, true));
        Arma objetivo = armaService.create(new Humo("Humo de cortina", 0, 300));
        Arma recargador = armaService.create(new Pistola("USP-S", 0, 500, 25, 70, 2.2, 400, false, true));
        return new SimulacionRequest(atacante.getId(), objetivo.getId(), recargador.getId(), 0, true, 10);
    }

    private EscenarioCombateRequest escenario(SimulacionRequest simulacion) {
        return new EscenarioCombateRequest(
                simulacion,
                new ExplosionA2Request(13, 0, 0, 0),
                new RafagaA3Request("rifle", 10, 3));
    }
}
