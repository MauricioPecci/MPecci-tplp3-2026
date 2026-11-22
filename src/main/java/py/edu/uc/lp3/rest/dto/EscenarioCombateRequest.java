package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Escenario de combate con validaciones cruzadas y simulación local")
public record EscenarioCombateRequest(
        @Schema(description = "Simulación que se persistirá en el H2 de A1") SimulacionRequest simulacionLocal,
        @Schema(description = "Regla de explosión que valida A2") ExplosionA2Request explosionA2,
        @Schema(description = "Regla de ráfaga que valida A3") RafagaA3Request rafagaA3) {
}
