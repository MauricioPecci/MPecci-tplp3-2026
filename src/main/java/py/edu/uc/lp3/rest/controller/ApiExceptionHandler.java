package py.edu.uc.lp3.rest.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ArmaInvalidaException.class)
    public ResponseEntity<Map<String, Object>> reglaDeDominioViolada(ArmaInvalidaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", e.getMessage(),
                        "rechazo", "el dominio no permite ese estado"));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> parametroDeUrlInvalido(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Parámetro de URL inválido: " + e.getMessage(),
                        "rechazo", "la URL no cumple el contrato del servicio"));
    }
}
