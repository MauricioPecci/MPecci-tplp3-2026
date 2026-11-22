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
import py.edu.uc.lp3.rest.dto.RafagaA3Request;
import reactor.core.publisher.Mono;

class CompaneroA3WebClientServiceTest {

    @Test
    void validarRafagaUsaLaRutaExternalizadaYDevuelveLaRespuesta() {
        AtomicReference<URI> uri = new AtomicReference<>();
        WebClient.Builder builder = WebClient.builder().exchangeFunction(request -> {
            uri.set(request.url());
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body("{\"estadoFinal\":\"30/87\",\"disparos\":[{\"exito\":true}]}")
                    .build());
        });
        CompaneroA3WebClientService servicio =
                new CompaneroA3WebClientService(builder, "https://a3.test", "/armas/{tipo}/disparar");

        Map<String, Object> respuesta = servicio.validarRafaga(new RafagaA3Request("rifle", 10, 3));

        assertThat(uri.get().toString()).contains("/armas/rifle/disparar")
                .contains("distancia=10.0")
                .contains("veces=3");
        assertThat(respuesta).containsEntry("estadoFinal", "30/87");
    }

    @Test
    void validarRafagaTraduceElRechazoDeA3() {
        String json = "{\"tipo\":\"IllegalArgumentException\",\"codigo\":400,\"error\":\"veces debe estar entre 1 y 100\"}";
        WebClient.Builder builder = WebClient.builder().exchangeFunction(request -> Mono.just(
                ClientResponse.create(HttpStatus.BAD_REQUEST)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(json)
                        .build()));
        CompaneroA3WebClientService servicio =
                new CompaneroA3WebClientService(builder, "https://a3.test", "/armas/{tipo}/disparar");

        assertThatThrownBy(() -> servicio.validarRafaga(new RafagaA3Request("rifle", 10, 0)))
                .isInstanceOf(ValidacionCompaneroRechazadaException.class)
                .hasMessageContaining("A3")
                .satisfies(error -> {
                    ValidacionCompaneroRechazadaException rechazo = (ValidacionCompaneroRechazadaException) error;
                    assertThat(rechazo.getCompanero()).isEqualTo("A3");
                    assertThat(rechazo.getEstadoExterno()).isEqualTo(400);
                    assertThat(rechazo.getCuerpoExterno()).contains("veces debe estar entre 1 y 100");
                });
    }
}
