package co.com.qvision.certificacion.regres.builders;

import co.com.qvision.certificacion.regres.intefaces.RegistraUsuarioInterface;
import co.com.qvision.certificacion.regres.models.RegistraUsuarioModel;

public class RegistraUsuarioBuilder implements RegistraUsuarioInterface {
    private String nombre;
    private String trabajo;

    public RegistraUsuarioBuilder(String nombre) {
        this.nombre = nombre;
    }

    public static RegistraUsuarioBuilder nombre(String nombre) {
        return new RegistraUsuarioBuilder(nombre);
    }

    public RegistraUsuarioModel yTrabajo(String trabajo) {
        this.trabajo = trabajo;
        return this.build();
    }

    @Override
    public RegistraUsuarioModel build() {
        RegistraUsuarioModel registrar = new RegistraUsuarioModel();
        registrar.setNombre(this.nombre);
        registrar.setTrabajo(this.trabajo);
        return registrar;
    }
}
