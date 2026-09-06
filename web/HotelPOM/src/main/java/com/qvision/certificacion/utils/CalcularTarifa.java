package com.qvision.certificacion.utils;


import static com.qvision.certificacion.utils.Constantes.NUMERO_DIAS;
import static com.qvision.certificacion.utils.Constantes.TARIFA_MAS_ECONOMICA;
import static net.serenitybdd.core.Serenity.sessionVariableCalled;

public class CalcularTarifa {

    public static String laTarifa() {
        return String.valueOf((Integer.parseInt(sessionVariableCalled(NUMERO_DIAS).toString()) + 1) * Integer.parseInt(sessionVariableCalled(TARIFA_MAS_ECONOMICA).toString()));
    }
}
