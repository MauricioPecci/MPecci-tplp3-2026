package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import py.edu.uc.lp3.exceptions.ArmaDominioException;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;
import py.edu.uc.lp3.exceptions.CargaMunicionException;
import py.edu.uc.lp3.exceptions.DanoInvalidoException;

@RestControllerAdvice
public class ApiExceptionHandler {

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

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> parametroDeUrlInvalido(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tipo", "PARAMETRO_INVALIDO");
        body.put("error", "Parámetro de URL inválido: " + e.getMessage());
        body.put("rechazo", "la URL no cumple el contrato del servicio");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
