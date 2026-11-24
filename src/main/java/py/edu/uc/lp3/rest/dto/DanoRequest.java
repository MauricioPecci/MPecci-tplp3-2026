package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para aplicar daño a un arma")
public record DanoRequest(
        @Schema(description = "Daño a aplicar", example = "40") Integer dano) {
}
