package com.qvision.certificacion.builders;

import com.qvision.certificacion.interfaces.IngresarLosDatosInterface;
import com.qvision.certificacion.models.IngresaLosDatosModel;

public class IngresarLosDatosBuilder implements IngresarLosDatosInterface {
    private String location;
    private String mesInicio;
    private String anoInicio;
    private String diaInicio;
    private String mesFin;
    private String anoFin;
    private String diaFin;
    private String rooms;
    private String adults;
    private String children;
    private IngresaLosDatosModel datos = new IngresaLosDatosModel();

    public IngresarLosDatosBuilder(String location) {
        this.location = location;
    }

    public static IngresarLosDatosBuilder location(String location) {
        return new IngresarLosDatosBuilder(location);
    }

    public IngresarLosDatosBuilder mesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
        return this;
    }

    public IngresarLosDatosBuilder anoInicio(String anoInicio) {
        this.anoInicio = anoInicio;
        return this;
    }

    public IngresarLosDatosBuilder mesFin(String mesFin) {
        this.mesFin = mesFin;
        return this;
    }

    public IngresarLosDatosBuilder anoFin(String anoFin) {
        this.anoFin = anoFin;
        return this;
    }

    public IngresarLosDatosBuilder diaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
        return this;
    }

    public IngresarLosDatosBuilder diaFin(String diaFin) {
        this.diaFin = diaFin;
        return this;
    }

    public IngresarLosDatosBuilder rooms(String rooms) {
        this.rooms = rooms;
        return this;
    }

    public IngresarLosDatosBuilder adults(String adults) {
        this.adults = adults;
        return this;
    }

    public IngresaLosDatosModel children(String children) {
        this.children = children;
        return this.build();
    }

    @Override
    public IngresaLosDatosModel build() {
        datos.setAdults(this.adults);
        datos.setAnoFin(this.anoFin);
        datos.setDiaFin(this.diaFin);
        datos.setMesFin(this.mesFin);
        datos.setChildren(this.children);
        datos.setDiaInicio(this.diaInicio);
        datos.setAnoInicio(this.anoInicio);
        datos.setMesInicio(this.mesInicio);
        datos.setLocation(this.location);
        datos.setRooms(this.rooms);
        return datos;
    }
}
