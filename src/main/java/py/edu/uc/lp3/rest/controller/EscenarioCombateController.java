package py.edu.uc.lp3.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.edu.uc.lp3.rest.dto.EscenarioCombateRequest;
import py.edu.uc.lp3.rest.dto.EscenarioCombateResponse;
import py.edu.uc.lp3.service.EscenarioCombateService;

@Tag(name = "Escenarios - APIs cruzadas", description = "Casos de uso que combinan reglas de A2 y A3 con la lógica local")
@RestController
@RequestMapping("/api/escenarios")
public class EscenarioCombateController {

    private final EscenarioCombateService escenarioCombateService;

    public EscenarioCombateController(EscenarioCombateService escenarioCombateService) {
        this.escenarioCombateService = escenarioCombateService;
    }

    @Operation(summary = "Combate validado por A2 y A3",
            description = "A2 valida una explosión HE sin detonarla, A3 valida una ráfaga de rifle y A1 persiste su simulación sólo si ambos aceptan.")
    @ApiResponse(responseCode = "201", description = "Escenario aceptado y simulación local persistida")
    @ApiResponse(responseCode = "400", description = "Regla de dominio o validación ajena rechazada")
    @ApiResponse(responseCode = "404", description = "Recurso no encontrado en A1 o en un compañero")
    @ApiResponse(responseCode = "409", description = "Conflicto con el estado de un compañero")
    @ApiResponse(responseCode = "502", description = "A2 o A3 no respondió")
    @PostMapping("/combate-validado")
    public ResponseEntity<EscenarioCombateResponse> combateValidado(@RequestBody EscenarioCombateRequest request) {
        EscenarioCombateResponse respuesta = escenarioCombateService.ejecutar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
