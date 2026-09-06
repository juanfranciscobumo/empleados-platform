package co.com.qvision.certificacion.regres.utils;

public enum CodigoDeRespuesta {
    OK(201, "HTTP/1.1 201 Created"),
    BAD_REQUEST(400, "Missing password");
    private int codigo;
    private String mensaje;


    CodigoDeRespuesta(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;

    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

}
