package com.sobreplanosstaging.herokuapp.builders;

import com.sobreplanosstaging.herokuapp.interfaces.FiltrosInterface;
import com.sobreplanosstaging.herokuapp.models.front.FiltrosModel;

public class FiltrosBuilder implements FiltrosInterface {
    private String bannos;
    private String areaMinima;
    private String areaMaxima;
    private String apartamento;
    private FiltrosModel filtros = new FiltrosModel();

    public FiltrosBuilder(String apartamento) {
        this.apartamento = apartamento;
    }

    public static FiltrosBuilder apartamento(String apartamento) {
        return new FiltrosBuilder(apartamento);
    }

    public FiltrosBuilder areaMinima(String areaMinima) {
        this.areaMinima = areaMinima;
        return this;
    }

    public FiltrosBuilder areaMaxima(String areaMaxima) {
        this.areaMaxima = areaMaxima;
        return this;
    }

    public FiltrosModel bannos(String bannos) {
        this.bannos = bannos;
        return this.build();
    }

    @Override
    public FiltrosModel build() {
        filtros.setBannos(this.bannos);
        filtros.setAreaMaxima(this.areaMaxima);
        filtros.setAreaMinima(this.areaMinima);
        filtros.setApartamento(this.apartamento);
        return filtros;
    }
}
