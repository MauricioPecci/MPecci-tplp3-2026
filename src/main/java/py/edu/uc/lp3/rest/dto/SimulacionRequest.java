package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de la simulación de combate")
public record SimulacionRequest(
        @Schema(description = "Id del arma atacante", example = "1") Integer atacanteId,
        @Schema(description = "Id del arma objetivo", example = "2") Integer objetivoId,
        @Schema(description = "Id del arma que recarga munición", example = "3") Integer recargadorId,
        @Schema(description = "Distancia del disparo", example = "100") int distancia,
        @Schema(description = "Indica si el disparo es crítico", example = "true") boolean critico,
        @Schema(description = "Munición a recargar", example = "10") int municion) {
}
