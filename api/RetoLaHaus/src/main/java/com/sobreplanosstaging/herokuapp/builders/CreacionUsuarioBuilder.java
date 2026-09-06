package com.sobreplanosstaging.herokuapp.builders;

import com.sobreplanosstaging.herokuapp.interfaces.CreacionUsuarioInterface;
import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;

public class CreacionUsuarioBuilder implements CreacionUsuarioInterface {
    private String name;
    private String job;
    private int id;

    public CreacionUsuarioBuilder(String name) {
        this.name = name;
    }

    public static CreacionUsuarioBuilder name(String name) {
        return new CreacionUsuarioBuilder(name);
    }

    public CreacionUsuarioBuilder id(int id) {
        this.id = id;
        return this;
    }

    public CreacionUsuarioRequest job(String job) {
        this.job = job;
        return this.build();
    }

    @Override
    public CreacionUsuarioRequest build() {
        CreacionUsuarioRequest usuario = new CreacionUsuarioRequest();
        usuario.setJob(this.job);
        usuario.setName(this.name);
        return usuario;
    }
}
