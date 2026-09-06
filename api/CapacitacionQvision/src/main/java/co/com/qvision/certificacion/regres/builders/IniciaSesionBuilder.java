package co.com.qvision.certificacion.regres.builders;


import co.com.qvision.certificacion.regres.intefaces.IniciaSesionInterface;
import co.com.qvision.certificacion.regres.models.IniciaSesionModel;

public class IniciaSesionBuilder implements IniciaSesionInterface {
    private String email;
    private String clave;
    public IniciaSesionBuilder(String email) {
        this.email = email;
    }

    public static IniciaSesionBuilder email(String email) {
        return new IniciaSesionBuilder(email);
    }
    public IniciaSesionModel yClave(String clave) {
        this.clave=clave;
        return this.build();
    }

    @Override
    public IniciaSesionModel build() {
        IniciaSesionModel iniciarSesion = new IniciaSesionModel();
        iniciarSesion.setContrasena(this.clave);
        iniciarSesion.setEmail(this.email);
        return iniciarSesion;
    }
}
