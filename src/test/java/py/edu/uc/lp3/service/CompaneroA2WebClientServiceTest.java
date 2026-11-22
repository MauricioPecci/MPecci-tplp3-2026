package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;
import py.edu.uc.lp3.rest.dto.ExplosionA2Request;
import reactor.core.publisher.Mono;

class CompaneroA2WebClientServiceTest {

    @Test
    void validarExplosionUsaLaRutaExternalizadaYDevuelveLaRespuesta() {
        AtomicReference<URI> uri = new AtomicReference<>();
        WebClient.Builder builder = WebClient.builder().exchangeFunction(request -> {
            uri.set(request.url());
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body("{\"granadaId\":13,\"simulacion\":true,\"resumen\":\"validado\"}")
                    .build());
        });
        CompaneroA2WebClientService servicio =
                new CompaneroA2WebClientService(builder, "https://a2.test", "/api/explosiones/validar");

        Map<String, Object> respuesta = servicio.validarExplosion(new ExplosionA2Request(13, 0, 0, 0));

        assertThat(uri.get().toString()).isEqualTo("https://a2.test/api/explosiones/validar");
        assertThat(respuesta).containsEntry("simulacion", true).containsEntry("resumen", "validado");
    }

    @Test
    void validarExplosionTraduceElRechazoDeA2() {
        String json = "{\"status\":400,\"tipo\":\"ArmaNoExplosivaException\"}";
        WebClient.Builder builder = WebClient.builder().exchangeFunction(request -> Mono.just(
                ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(json)
                        .build()));
        CompaneroA2WebClientService servicio =
                new CompaneroA2WebClientService(builder, "https://a2.test", "/api/explosiones/validar");

        assertThatThrownBy(() -> servicio.validarExplosion(new ExplosionA2Request(3, 0, 0, 0)))
                .isInstanceOf(ValidacionCompaneroRechazadaException.class)
                .hasMessageContaining("A2")
                .satisfies(error -> {
                    ValidacionCompaneroRechazadaException rechazo = (ValidacionCompaneroRechazadaException) error;
                    assertThat(rechazo.getCompanero()).isEqualTo("A2");
                    assertThat(rechazo.getEstadoExterno()).isEqualTo(400);
                    assertThat(rechazo.getCuerpoExterno()).contains("ArmaNoExplosivaException");
                });
    }
}
