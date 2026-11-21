package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de la simulación de combate")
public record SimulacionResponse(
        @Schema(description = "Clase concreta del atacante") String atacanteClase,
        @Schema(description = "Munición del atacante antes de disparar") int atacanteMunicionAntes,
        @Schema(description = "Munición del atacante después de disparar") int atacanteMunicionDespues,
        @Schema(description = "Clase concreta del objetivo") String objetivoClase,
        @Schema(description = "Durabilidad del objetivo antes del disparo") int objetivoDurabilidadAntes,
        @Schema(description = "Durabilidad del objetivo después del disparo") int objetivoDurabilidadDespues,
        @Schema(description = "Clase concreta del recargador") String recargadorClase,
        @Schema(description = "Munición del recargador antes de recargar") int recargadorMunicionAntes,
        @Schema(description = "Munición del recargador después de recargar") int recargadorMunicionDespues,
        @Schema(description = "Daño aplicado al objetivo") int danoAplicado,
        @Schema(description = "Munición recargada") int municionRecargada,
        @Schema(description = "Indica si el objetivo fue destruido") boolean objetivoDestruido) {
}
