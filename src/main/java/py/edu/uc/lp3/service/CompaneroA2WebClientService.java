package py.edu.uc.lp3.service;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exceptions.ValidacionCompaneroRechazadaException;
import py.edu.uc.lp3.rest.dto.ExplosionA2Request;

@Service
public class CompaneroA2WebClientService implements CompaneroA2Client {

    private static final Logger LOGGER = LogManager.getLogger(CompaneroA2WebClientService.class);

    private final WebClient webClient;
    private final String validarPath;

    public CompaneroA2WebClientService(WebClient.Builder webClientBuilder,
            @Value("${api.a2.base-url}") String baseUrl,
            @Value("${api.a2.explosion-validar-path}") String validarPath) {
        this.webClient = webClientBuilder
                .filter(registrarIntercambio())
                .baseUrl(baseUrl)
                .build();
        this.validarPath = validarPath;
        LOGGER.debug("Cliente A2 configurado con base {} y ruta {}", baseUrl, validarPath);
    }

    @Override
    public Map<String, Object> validarExplosion(ExplosionA2Request request) {
        if (request == null || request.granadaId() == null || request.granadaId() <= 0) {
            LOGGER.debug("Validación local de A2 rechazada: el id de la granada debe ser mayor a 0");
            throw new ArmaInvalidaException("El id de la granada de A2 debe ser mayor a 0");
        }
        LOGGER.debug("Validando explosión en A2: granadaId={}, centro=({},{},{})",
                request.granadaId(), request.x(), request.y(), request.z());
        try {
            Map<String, Object> respuesta = webClient.post()
                    .uri(validarPath)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .defaultIfEmpty(Map.of())
                    .block();
            LOGGER.debug("Cuerpo de respuesta de A2 para la granada {}: {}", request.granadaId(), respuesta);
            LOGGER.debug("A2 aceptó la explosión de la granada {}", request.granadaId());
            return respuesta;
        } catch (WebClientResponseException e) {
            LOGGER.debug("A2 rechazó la explosión: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ValidacionCompaneroRechazadaException("A2", e.getStatusCode().value(),
                    e.getResponseBodyAsString());
        } catch (WebClientException e) {
            LOGGER.debug("A2 no respondió: {}", e.toString());
            throw new CompaneroNoDisponibleException("A2", e);
        }
    }

    private ExchangeFilterFunction registrarIntercambio() {
        return (request, next) -> {
            LOGGER.debug("Solicitud A2 preparada: {} {}", request.method(), request.url());
            return next.exchange(request)
                    .doOnNext(response -> LOGGER.debug("Respuesta A2 recibida: status {}", response.statusCode()))
                    .doOnError(error -> LOGGER.debug("Error de transporte con A2: {}", error.toString()));
        };
    }
}
