package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para cargar munición de un arma")
public record CargaMunicionRequest(
        @Schema(description = "Cantidad de munición a cargar", example = "10") Integer cantidad) {
}
