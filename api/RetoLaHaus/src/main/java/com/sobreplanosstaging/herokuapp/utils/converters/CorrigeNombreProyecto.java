package com.sobreplanosstaging.herokuapp.utils.converters;

public class CorrigeNombreProyecto {
    private CorrigeNombreProyecto() {

    }

    public static String corregirNombreProyecto(String nombre) {
        String str = nombre.toLowerCase();
        String firstLtr = str.substring(0, 1);
        String restLtrs = str.substring(1);

        firstLtr = firstLtr.toUpperCase();
        str = firstLtr + restLtrs;
        return str;
    }
}
