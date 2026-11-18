package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Objeto devuelto por la API externa restful-api.dev")
public record ObjetoExterno(
        @Schema(description = "Identificador del objeto", example = "1") String id,
        @Schema(description = "Nombre del objeto", example = "Google Pixel 6 Pro") String name,
        @Schema(description = "Atributos libres del objeto") Map<String, Object> data,
        @Schema(description = "Fecha de creación, si la API la informa") String createdAt) {
}
