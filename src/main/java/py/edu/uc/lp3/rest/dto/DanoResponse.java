package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de aplicar daño a un arma")
public record DanoResponse(
        @Schema(description = "Id del arma") Integer id,
        @Schema(description = "Nombre del arma") String nombre,
        @Schema(description = "Durabilidad restante del arma") int durabilidad,
        @Schema(description = "Daño efectivamente aplicado") int danoAplicado,
        @Schema(description = "Indica si el arma fue destruida") boolean destruida) {
}
