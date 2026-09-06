package co.com.qvision.certificacion.regres.questions;

import static co.com.qvision.certificacion.regres.utils.ObtieneLaFecha.getFechaDelServicio;
import static net.serenitybdd.rest.SerenityRest.lastResponse;

import co.com.qvision.certificacion.regres.models.RespuestaCreacionDeUsuario;
import net.serenitybdd.screenplay.Question;

public class LaCreacionDelUsuario {
    private LaCreacionDelUsuario() {

    }

    public static Question<String> conNombre() {
        return actor -> lastResponse().jsonPath().getObject("", RespuestaCreacionDeUsuario.class).getNombre();
    }

    public static Question<String> conTrabajo() {
        return actor -> lastResponse().jsonPath().getString("trabajo");
    }

    public static Question<String> enLaFecha() {
        return actor -> getFechaDelServicio(lastResponse().jsonPath().getObject("", RespuestaCreacionDeUsuario.class).getCreatedAt());
    }
}
