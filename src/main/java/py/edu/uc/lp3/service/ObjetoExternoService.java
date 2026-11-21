package py.edu.uc.lp3.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import py.edu.uc.lp3.rest.dto.ObjetoExterno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ObjetoExternoService {

    private static final Logger LOGGER = LogManager.getLogger(ObjetoExternoService.class);

    private final WebClient webClient;
    private final String baseUrl;

    public ObjetoExternoService(WebClient.Builder webClientBuilder,
            @Value("${api.restful.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder
                .filter(registrarIntercambio())
                .baseUrl(baseUrl)
                .build();
        LOGGER.debug("WebClient configurado con URL base {}", baseUrl);
    }

    private ExchangeFilterFunction registrarIntercambio() {
        return (request, next) -> {
            LOGGER.debug("Solicitud HTTP preparada: {} {}", request.method(), request.url());
            return next.exchange(request)
                    .doOnNext(response -> LOGGER.debug("Respuesta HTTP recibida: status {}", response.statusCode()))
                    .doOnError(error -> LOGGER.debug("Error de transporte HTTP: {}", error.toString()));
        };
    }

    public Flux<ObjetoExterno> findAll() {
        LOGGER.debug("Iniciando consulta a la API externa: GET {}/objects", baseUrl);
        return Flux.defer(() -> {
            AtomicInteger cantidad = new AtomicInteger();
            return webClient.get()
                    .uri("/objects")
                    .retrieve()
                    .bodyToFlux(ObjetoExterno.class)
                    .doOnNext(objeto -> {
                        LOGGER.debug("Objeto deserializado: id={}, name={}", objeto.id(), objeto.name());
                        cantidad.incrementAndGet();
                    })
                    .doOnComplete(() -> LOGGER.debug("Consulta finalizada: {} objetos obtenidos", cantidad.get()))
                    .doOnError(error -> LOGGER.debug("Error al consultar la API externa: {}", error.toString()))
                    .doFinally(senal -> LOGGER.debug("Consulta concluida con señal {}", senal));
        });
    }

    public Mono<ObjetoExterno> findById(String id) {
        LOGGER.debug("Iniciando consulta a la API externa: GET {}/objects/{}", baseUrl, id);
        if (id == null || id.isBlank()) {
            LOGGER.debug("Consulta rechazada: el id del objeto está vacío");
            throw new IllegalArgumentException("El id del objeto no puede estar vacío");
        }
        return webClient.get()
                .uri("/objects/{id}", id)
                .retrieve()
                .bodyToMono(ObjetoExterno.class)
                .doOnNext(objeto -> LOGGER.debug("Objeto deserializado: id={}, name={}", objeto.id(), objeto.name()))
                .doOnError(error -> LOGGER.debug("Error al consultar la API externa: {}", error.toString()))
                .doFinally(senal -> LOGGER.debug("Consulta concluida con señal {}", senal));
    }
}
