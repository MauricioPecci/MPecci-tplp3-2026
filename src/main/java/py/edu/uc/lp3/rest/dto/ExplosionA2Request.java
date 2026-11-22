package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pedido de validación de explosión según el OpenAPI de A2")
public record ExplosionA2Request(
        @Schema(description = "Id de la granada HE en A2", example = "13") Integer granadaId,
        @Schema(description = "Coordenada x del centro", example = "0") double x,
        @Schema(description = "Coordenada y del centro", example = "0") double y,
        @Schema(description = "Coordenada z del centro", example = "0") double z) {
}
