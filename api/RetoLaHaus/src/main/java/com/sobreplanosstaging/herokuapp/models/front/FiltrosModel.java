package com.sobreplanosstaging.herokuapp.models.front;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FiltrosModel extends BusquedaModel {
    @JsonAlias("baños")
    private String bannos;
    private String areaMinima;
    private String areaMaxima;
    private String apartamento;

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public String getAreaMinima() {
        return areaMinima;
    }

    public void setAreaMinima(String areaMinima) {
        this.areaMinima = areaMinima;
    }

    public String getAreaMaxima() {
        return areaMaxima;
    }

    public void setAreaMaxima(String areaMaxima) {
        this.areaMaxima = areaMaxima;
    }

    public String getBannos() {
        return bannos;
    }

    public void setBannos(String bannos) {
        this.bannos = bannos;
    }
}
