package py.edu.uc.lp3.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import py.edu.uc.lp3.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;

class ApiExceptionHandlerCompaneroTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void rechazoA2Con409ConservaElEstadoExterno() {
        ResponseEntity<Map<String, Object>> respuesta = handler.validacionCompaneroRechazada(
                new ValidacionCompaneroRechazadaException("A2", 409, "granada ya detonada"));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        assertThat(respuesta.getBody())
                .containsEntry("tipo", "RECHAZO_A2")
                .containsEntry("companero", "A2")
                .containsEntry("codigoExterno", 409)
                .containsEntry("cuerpoExterno", "granada ya detonada");
    }

    @Test
    void rechazoA3Con400ConservaElCuerpoExterno() {
        ResponseEntity<Map<String, Object>> respuesta = handler.validacionCompaneroRechazada(
                new ValidacionCompaneroRechazadaException("A3", 400, "veces fuera de rango"));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
        assertThat(respuesta.getBody())
                .containsEntry("tipo", "RECHAZO_A3")
                .containsEntry("codigoExterno", 400)
                .containsEntry("cuerpoExterno", "veces fuera de rango");
    }

    @Test
    void companiaNoDisponibleDevuelve502() {
        ResponseEntity<Map<String, Object>> respuesta = handler.companeroNoDisponible(
                new CompaneroNoDisponibleException("A3", new RuntimeException("timeout")));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(502);
        assertThat(respuesta.getBody())
                .containsEntry("tipo", "SERVICIO_COMPANERO_NO_DISPONIBLE")
                .containsEntry("companero", "A3");
    }
}
