package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import py.edu.uc.lp3.exceptions.ArmaDominioException;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.ArmaNoEncontradaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ArmaNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> armaNoEncontrada(ArmaNoEncontradaException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "ARMA_NO_ENCONTRADA");
        body.put("error", e.getMessage());
        body.put("rechazo", "el recurso solicitado no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ArmaInvalidaException.class)
    public ResponseEntity<Map<String, Object>> armaInvalida(ArmaInvalidaException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "ARMA_INVALIDA");
        body.put("error", e.getMessage());
        body.put("rechazo", "el dominio no permite ese estado");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DanoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> danoInvalido(DanoInvalidoException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "DANO_INVALIDO");
        body.put("error", e.getMessage());
        body.put("rechazo", "el valor de daño no es válido para esta arma");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CargaMunicionException.class)
    public ResponseEntity<Map<String, Object>> cargaMunicionInvalida(CargaMunicionException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "CARGA_MUNICION_INVALIDA");
        body.put("error", e.getMessage());
        body.put("rechazo", "la operación de munición viola la capacidad del arma");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ArmaDominioException.class)
    public ResponseEntity<Map<String, Object>> dominioGeneral(ArmaDominioException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "DOMINIO");
        body.put("error", e.getMessage());
        body.put("rechazo", "regla de dominio violada");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<Map<String, Object>> objetoExternoNoEncontrado(NotFound e) {
        LOGGER.debug("La API externa respondió 404: {}", e.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "OBJETO_EXTERNO_NO_ENCONTRADO");
        body.put("error", e.getMessage());
        body.put("rechazo", "el objeto no existe en la API externa");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Map<String, Object>> servicioExternoNoDisponible(WebClientException e) {
        LOGGER.debug("Falló la integración con la API externa: {}", e.toString());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "SERVICIO_EXTERNO_NO_DISPONIBLE");
        body.put("error", e.getMessage());
        body.put("rechazo", "la API externa no pudo completar la consulta");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> cuerpoInvalido(HttpMessageNotReadableException e) {
        Throwable causa = e.getCause();
        while (causa != null) {
            if (causa instanceof DanoInvalidoException danoInvalido) {
                return danoInvalido(danoInvalido);
            }
            if (causa instanceof CargaMunicionException cargaInvalida) {
                return cargaMunicionInvalida(cargaInvalida);
            }
            if (causa instanceof ArmaNoEncontradaException noEncontrada) {
                return armaNoEncontrada(noEncontrada);
            }
            if (causa instanceof ArmaInvalidaException invalida) {
                return armaInvalida(invalida);
            }
            if (causa instanceof ArmaDominioException dominio) {
                return dominioGeneral(dominio);
            }
            causa = causa.getCause();
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "CUERPO_INVALIDO");
        body.put("error", "Cuerpo de la petición inválido o no legible");
        body.put("rechazo", "el cuerpo no cumple el contrato del servicio");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> parametroDeUrlInvalido(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "PARAMETRO_INVALIDO");
        body.put("error", "Parámetro de URL inválido: " + e.getMessage());
        body.put("rechazo", "la URL no cumple el contrato del servicio");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
