package py.edu.uc.lp3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import py.edu.uc.lp3.rest.dto.ObjetoExterno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ObjetoExternoService {

    private final WebClient webClient;

    public ObjetoExternoService(WebClient.Builder webClientBuilder,
            @Value("${api.restful.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Flux<ObjetoExterno> findAll() {
        return webClient.get()
                .uri("/objects")
                .retrieve()
                .bodyToFlux(ObjetoExterno.class);
    }

    public Mono<ObjetoExterno> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del objeto no puede estar vacío");
        }
        return webClient.get()
                .uri("/objects/{id}", id)
                .retrieve()
                .bodyToMono(ObjetoExterno.class);
    }
}
