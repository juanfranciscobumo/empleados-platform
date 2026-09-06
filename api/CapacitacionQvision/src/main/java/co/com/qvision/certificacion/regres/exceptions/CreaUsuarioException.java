package co.com.qvision.certificacion.regres.exceptions;

public class CreaUsuarioException extends AssertionError {
    public static final String CODIGO_RESPUESTA = "EL CODIGO DE RESPUESTA ES INCORRECTO";
    public static final String NOMBRE = "EL NOMBRE NO COINCIDE";
    public static final String TRABAJO = "EL TRABAJO NO COINCIDE";
    public static final String FECHA = "LA FECHA NO COINCIDE";

    public CreaUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
}
