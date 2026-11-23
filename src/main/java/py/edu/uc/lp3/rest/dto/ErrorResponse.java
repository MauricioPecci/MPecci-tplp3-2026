package py.edu.uc.lp3.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo de error devuelto por A1 cuando falla una regla local o ajena")
public record ErrorResponse(
        @Schema(description = "Tipo de error: ARMA_INVALIDA, RECHAZO_A2, RECHAZO_A3, SERVICIO_COMPANERO_NO_DISPONIBLE")
        String tipo,
        @Schema(description = "Mensaje legible del error") String error,
        @Schema(description = "Motivo del rechazo") String rechazo,
        @Schema(description = "Compañero A2 o A3 cuando corresponde") String companero,
        @Schema(description = "Código HTTP devuelto por el compañero") Integer codigoExterno,
        @Schema(description = "Cuerpo devuelto por el compañero") String cuerpoExterno) {
}
