package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pedido de validación de ráfaga según el OpenAPI de A3")
public record RafagaA3Request(
        @Schema(description = "Tipo de arma en A3", example = "rifle") String tipo,
        @Schema(description = "Distancia del disparo", example = "10") double distancia,
        @Schema(description = "Cantidad de disparos", example = "3") int veces) {
}
