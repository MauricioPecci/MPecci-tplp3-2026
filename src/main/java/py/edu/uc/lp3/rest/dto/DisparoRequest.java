package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para disparar con un arma de fuego")
public record DisparoRequest(
        @Schema(description = "Id del arma objetivo", example = "2") Integer objetivoId,
        @Schema(description = "Distancia del disparo", example = "0") int distancia,
        @Schema(description = "Indica si el disparo es crítico", example = "false") boolean critico) {
}
