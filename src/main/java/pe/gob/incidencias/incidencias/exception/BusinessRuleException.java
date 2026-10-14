package pe.gob.incidencias.incidencias.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}
