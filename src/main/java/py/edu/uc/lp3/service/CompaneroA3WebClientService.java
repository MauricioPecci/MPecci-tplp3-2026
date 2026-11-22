package py.edu.uc.lp3.service;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;
import py.edu.uc.lp3.rest.dto.RafagaA3Request;

@Service
public class CompaneroA3WebClientService implements CompaneroA3Client {

    private static final Logger LOGGER = LogManager.getLogger(CompaneroA3WebClientService.class);

    private final WebClient webClient;
    private final String rafagaPath;

    public CompaneroA3WebClientService(WebClient.Builder webClientBuilder,
            @Value("${api.a3.base-url}") String baseUrl,
            @Value("${api.a3.rafaga-path}") String rafagaPath) {
        this.webClient = webClientBuilder
                .filter(registrarIntercambio())
                .baseUrl(baseUrl)
                .build();
        this.rafagaPath = rafagaPath;
        LOGGER.debug("Cliente A3 configurado con base {} y ruta {}", baseUrl, rafagaPath);
    }

    @Override
    public Map<String, Object> validarRafaga(RafagaA3Request request) {
        if (request == null || request.tipo() == null || request.tipo().isBlank()) {
            throw new ArmaInvalidaException("El tipo de arma de A3 es obligatorio");
        }
        LOGGER.debug("Validando ráfaga en A3: tipo={}, distancia={}, veces={}",
                request.tipo(), request.distancia(), request.veces());
        try {
            Map<String, Object> respuesta = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(rafagaPath)
                            .queryParam("distancia", request.distancia())
                            .queryParam("veces", request.veces())
                            .build(Map.of("tipo", request.tipo())))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .defaultIfEmpty(Map.of())
                    .block();
            LOGGER.debug("A3 aceptó la ráfaga de {}", request.tipo());
            return respuesta;
        } catch (WebClientResponseException e) {
            LOGGER.debug("A3 rechazó la ráfaga: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ValidacionCompaneroRechazadaException("A3", e.getStatusCode().value(),
                    e.getResponseBodyAsString());
        } catch (WebClientException e) {
            LOGGER.debug("A3 no respondió: {}", e.toString());
            throw new CompaneroNoDisponibleException("A3", e);
        }
    }

    private ExchangeFilterFunction registrarIntercambio() {
        return (request, next) -> {
            LOGGER.debug("Solicitud A3 preparada: {} {}", request.method(), request.url());
            return next.exchange(request)
                    .doOnNext(response -> LOGGER.debug("Respuesta A3 recibida: status {}", response.statusCode()))
                    .doOnError(error -> LOGGER.debug("Error de transporte con A3: {}", error.toString()));
        };
    }
}
