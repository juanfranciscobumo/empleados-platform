package com.qvision.certificacion.utils;

import java.util.stream.Collectors;

import static com.qvision.certificacion.userinterfaces.PreciosHotelPages.ltPrecios;
import static com.qvision.certificacion.utils.Constantes.TARIFA_MAS_ECONOMICA;
import static net.serenitybdd.core.Serenity.setSessionVariable;

public class PrecioMasBajo {

    public static int obtenerPrecioMasBajo() {
        int precio = ltPrecios.stream().map(value -> value.getText().replace("$", "")).collect(Collectors.toList()).stream().mapToInt(Integer::parseInt).min().getAsInt();
        setSessionVariable(TARIFA_MAS_ECONOMICA).to(precio);
        return precio;

    }

}
