package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de un disparo")
public record DisparoResponse(
        @Schema(description = "Id del arma atacante") Integer atacanteId,
        @Schema(description = "Nombre del arma atacante") String atacanteNombre,
        @Schema(description = "Munición del atacante antes de disparar") int atacanteMunicionAntes,
        @Schema(description = "Munición del atacante después de disparar") int atacanteMunicionDespues,
        @Schema(description = "Id del arma objetivo") Integer objetivoId,
        @Schema(description = "Nombre del arma objetivo") String objetivoNombre,
        @Schema(description = "Durabilidad del objetivo antes del disparo") int objetivoDurabilidadAntes,
        @Schema(description = "Durabilidad del objetivo después del disparo") int objetivoDurabilidadDespues,
        @Schema(description = "Daño efectivamente aplicado") int danoAplicado,
        @Schema(description = "Indica si el objetivo fue destruido") boolean objetivoDestruido) {
}
