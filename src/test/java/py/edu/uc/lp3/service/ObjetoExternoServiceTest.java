package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import py.edu.uc.lp3.rest.dto.ObjetoExterno;
import reactor.core.publisher.Mono;

class ObjetoExternoServiceTest {

    @Test
    void findAllDeserializaLosObjetosDeLaApi() {
        String json = """
                [
                  {"id":"1","name":"Google Pixel 6 Pro","data":{"color":"Cloudy White","capacity":"128 GB"}},
                  {"id":"2","name":"Apple iPhone 12 Mini","data":null}
                ]
                """;

        List<ObjetoExterno> objetos = servicioQueResponde(HttpStatus.OK, json)
                .findAll()
                .collectList()
                .block();

        assertThat(objetos).hasSize(2);
        assertThat(objetos.get(0).id()).isEqualTo("1");
        assertThat(objetos.get(0).data()).containsEntry("color", "Cloudy White");
        assertThat(objetos.get(1).data()).isNull();
    }

    @Test
    void findByIdDeserializaUnObjeto() {
        String json = """
                {"id":"3","name":"Apple iPhone 12 Pro Max","data":{"capacity GB":512}}
                """;

        ObjetoExterno objeto = servicioQueResponde(HttpStatus.OK, json).findById("3").block();

        assertThat(objeto.name()).isEqualTo("Apple iPhone 12 Pro Max");
        assertThat(objeto.data()).containsEntry("capacity GB", 512);
    }

    @Test
    void findByIdPropagaElErrorDeLaApiExterna() {
        ObjetoExternoService servicio = servicioQueResponde(HttpStatus.NOT_FOUND, "{}");

        assertThatThrownBy(() -> servicio.findById("999").block())
                .isInstanceOf(NotFound.class);
    }

    @Test
    void findByIdRechazaUnIdVacio() {
        ObjetoExternoService servicio = servicioQueResponde(HttpStatus.OK, "{}");

        assertThatThrownBy(() -> servicio.findById(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede estar vacío");
    }

    private ObjetoExternoService servicioQueResponde(HttpStatus estado, String json) {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(request -> Mono.just(ClientResponse.create(estado)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .body(json)
                        .build()));
        return new ObjetoExternoService(builder, "https://api.restful-api.dev");
    }
}
