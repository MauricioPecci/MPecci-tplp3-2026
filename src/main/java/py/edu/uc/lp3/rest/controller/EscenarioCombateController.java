package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.rest.dto.ErrorResponse;
import py.edu.uc.lp3.rest.dto.EscenarioCombateRequest;
import py.edu.uc.lp3.rest.dto.EscenarioCombateResponse;
import py.edu.uc.lp3.service.EscenarioCombateService;

@Tag(name = "Escenarios - APIs cruzadas", description = "Casos de uso que combinan reglas de A2 y A3 con la lógica local")
@RestController
@RequestMapping("/api/escenarios")
public class EscenarioCombateController {

    private static final Logger LOGGER = LogManager.getLogger(EscenarioCombateController.class);

    private final EscenarioCombateService escenarioCombateService;

    public EscenarioCombateController(EscenarioCombateService escenarioCombateService) {
        this.escenarioCombateService = escenarioCombateService;
    }

    @Operation(summary = "Combate validado por A2 y A3",
            description = "A2 valida una explosión HE sin detonarla, A3 valida una ráfaga de rifle y A1 persiste su simulación sólo si ambos aceptan.")
    @ApiResponse(responseCode = "201", description = "Escenario aceptado y simulación local persistida")
    @ApiResponse(responseCode = "400", description = "Regla de dominio o validación ajena rechazada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Recurso no encontrado en A1 o en un compañero",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con el estado de un compañero",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "502", description = "A2 o A3 no respondió",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/combate-validado")
    public ResponseEntity<EscenarioCombateResponse> combateValidado(@RequestBody EscenarioCombateRequest request) {
        LOGGER.debug("POST /api/escenarios/combate-validado recibido");
        EscenarioCombateResponse respuesta = escenarioCombateService.ejecutar(request);
        LOGGER.debug("POST /api/escenarios/combate-validado respondiendo 201");
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
