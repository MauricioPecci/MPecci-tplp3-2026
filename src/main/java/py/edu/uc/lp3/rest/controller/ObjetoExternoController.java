package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.rest.dto.ObjetoExterno;
import py.edu.uc.lp3.service.ObjetoExternoService;
import reactor.core.publisher.Mono;

@Tag(name = "Objetos externos", description = "Consulta a la API pública restful-api.dev mediante WebClient")
@RestController
@RequestMapping("/api/objetos")
public class ObjetoExternoController {

    private static final Logger LOGGER = LogManager.getLogger(ObjetoExternoController.class);

    private final ObjetoExternoService objetoExternoService;

    public ObjetoExternoController(ObjetoExternoService objetoExternoService) {
        this.objetoExternoService = objetoExternoService;
    }

    @Operation(summary = "Listar objetos externos", description = "Obtiene todos los objetos de GET /objects de la API externa.")
    @ApiResponse(responseCode = "502", description = "La API externa no pudo completar la consulta")
    @GetMapping
    public Mono<List<ObjetoExterno>> findAll() {
        LOGGER.debug("Solicitud recibida: GET /api/objetos");
        return objetoExternoService.findAll()
                .collectList()
                .doOnNext(objetos -> LOGGER.debug("Respuesta a enviar con {} objetos externos", objetos.size()));
    }

    @Operation(summary = "Obtener objeto externo por id", description = "Obtiene un objeto de GET /objects/{id} de la API externa.")
    @ApiResponse(responseCode = "404", description = "El objeto no existe en la API externa")
    @ApiResponse(responseCode = "502", description = "La API externa no pudo completar la consulta")
    @GetMapping("/{id}")
    public Mono<ObjetoExterno> findById(@Parameter(description = "Identificador del objeto externo") @PathVariable String id) {
        LOGGER.debug("Solicitud recibida: GET /api/objetos/{}", id);
        return objetoExternoService.findById(id)
                .doOnNext(objeto -> LOGGER.debug("Respuesta a enviar con el objeto externo id={}", objeto.id()));
    }
}
