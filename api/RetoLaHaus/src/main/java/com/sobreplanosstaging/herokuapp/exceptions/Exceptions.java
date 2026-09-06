package com.sobreplanosstaging.herokuapp.exceptions;

@SuppressWarnings("serial")
public class Exceptions extends AssertionError {
    public static final String CODIGO_USUARIO = "Se present� un error con el código del usuario";
    public static final String CODIGO_DE_RESPUESTA = "El código de respuesta es diferente al esperado";
    public static final String HABITACIONES = "Se presentó un error con el campo habitaciones";
    public static final String BANNOS = "Se presentó un error con el campo Baños";

    public static final String PARQUEADEROS = "Se presentó un error con el campo parqueaderos";
    public static final String NOMBRE_PROYECTO = "Se presentó un error con el campo nombre del proyecto";
    public static final String CREACION_DE_USUARIO = "Error al comparar la creación del usuario";

    public Exceptions(String message, Throwable cause) {
        super(message, cause);

    }
}
