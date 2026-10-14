package pe.gob.incidencias.incidencias.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduce excepciones a respuestas JSON limpias.
 * No se captura Exception genérica a propósito: así AccessDeniedException
 * sigue llegando a Spring Security y responde 403 en lugar de 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> noEncontrado(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> reglaNegocio(BusinessRuleException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> autenticacion(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errores.putIfAbsent(fe.getField(), fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos", errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> cuerpoInvalido(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST,
                "Cuerpo de la solicitud inválido (revisa el JSON y los valores del estado)", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Valor inválido para el parámetro '" + ex.getName() + "'", null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridad(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT,
                "La operación viola una restricción de datos (registro duplicado o en uso)", null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String mensaje, Map<String, String> errores) {
        return ResponseEntity.status(status)
                .body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), mensaje, errores));
    }
}
