package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Resultado del escenario con las respuestas de los compañeros")
public record EscenarioCombateResponse(
        @Schema(description = "Nombre del caso de uso") String escenario,
        @Schema(description = "Resultado persistido por A1") SimulacionResponse simulacionLocal,
        @Schema(description = "Respuesta devuelta por A2") Map<String, Object> validacionA2,
        @Schema(description = "Respuesta devuelta por A3") Map<String, Object> validacionA3) {
}
