package com.sobreplanosstaging.herokuapp.builders;

import com.sobreplanosstaging.herokuapp.interfaces.BusquedaInterface;
import com.sobreplanosstaging.herokuapp.models.front.BusquedaModel;

public class BusquedaBuilder implements BusquedaInterface {
    private String barrio;
    private String tipoPropiedad;
    private String habitaciones;
    private BusquedaModel busqueda = new BusquedaModel();

    public BusquedaBuilder(String tipoPropiedad) {
        this.tipoPropiedad = tipoPropiedad;
    }

    public static BusquedaBuilder tipoDePropiedad(String tipoPropiedad) {
        return new BusquedaBuilder(tipoPropiedad);
    }

    public BusquedaBuilder barrio(String barrio) {
        this.barrio = barrio;
        return this;
    }

    public BusquedaModel habitaciones(String habitaciones) {
        this.habitaciones = habitaciones;
        return this.build();
    }

    @Override
    public BusquedaModel build() {
        busqueda.setTipoPropiedad(this.tipoPropiedad);
        busqueda.setHabitaciones(this.habitaciones);
        busqueda.setBarrio(this.barrio);
        return busqueda;
    }
}
