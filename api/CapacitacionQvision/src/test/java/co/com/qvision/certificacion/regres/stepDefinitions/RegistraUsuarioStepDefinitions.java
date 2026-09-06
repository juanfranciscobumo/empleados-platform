package co.com.qvision.certificacion.regres.stepDefinitions;

import co.com.qvision.certificacion.regres.abilities.LeeUnExcel;
import co.com.qvision.certificacion.regres.exceptions.CreaUsuarioException;
import co.com.qvision.certificacion.regres.models.IniciaSesionModel;
import co.com.qvision.certificacion.regres.questions.LaCreacionDelUsuario;
import co.com.qvision.certificacion.regres.tasks.IniciaSesion;
import co.com.qvision.certificacion.regres.tasks.RegistraElUsuario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.List;

import static co.com.qvision.certificacion.regres.builders.IniciaSesionBuilder.email;
import static co.com.qvision.certificacion.regres.builders.RegistraUsuarioBuilder.nombre;
import static co.com.qvision.certificacion.regres.exceptions.CreaUsuarioException.CODIGO_RESPUESTA;
import static co.com.qvision.certificacion.regres.exceptions.CreaUsuarioException.FECHA;
import static co.com.qvision.certificacion.regres.exceptions.CreaUsuarioException.NOMBRE;
import static co.com.qvision.certificacion.regres.exceptions.CreaUsuarioException.TRABAJO;
import static co.com.qvision.certificacion.regres.utils.CodigoDeRespuesta.valueOf;
import static co.com.qvision.certificacion.regres.utils.Constantes.MENSAJE_RESPUESTA;
import static co.com.qvision.certificacion.regres.utils.ObtieneLaFecha.getFechaDelSistema;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

public class RegistraUsuarioStepDefinitions {
    private static final String ERROR = "error";
    private static final String ACTOR = "Juan";
    private static final String ENDPOINT_REGRES = "endpoint.registro";

    public EnvironmentVariables environmentVariables;
    public String endpoint;

    @Dado("que se haya ingresado el endpoint del servicio")
    public void queSeHayaIngresadoElEndpointDelServicio() {
        endpoint = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty(ENDPOINT_REGRES);
        theActorCalled(ACTOR).can(CallAnApi.at(endpoint));
    }

    @Cuando("el usuario {string} ingrese su trabajo {string}")
    public void elUsuarioIngreseSuTrabajo(String nombre, String trabajo) {
        theActorInTheSpotlight().attemptsTo(RegistraElUsuario
                .conLosDatos(nombre(nombre).yTrabajo(trabajo)));
    }

    @Entonces("el usuario {string} vera el trabajo {string} creado exitosamente {string}")
    public void elUsuarioVeraElTrabajoCreadoExitosamente(String nombre, String trabajo, String codigoRespuesta) {
        theActorInTheSpotlight().should(
                seeThatResponse(MENSAJE_RESPUESTA,
                        response -> response
                                .statusCode(valueOf(codigoRespuesta).getCodigo())
                                .statusLine(valueOf(codigoRespuesta).getMensaje()))
                        .orComplainWith(CreaUsuarioException.class, CODIGO_RESPUESTA),
                seeThat("El nombre", LaCreacionDelUsuario.conNombre(), equalTo(nombre))
                        .orComplainWith(CreaUsuarioException.class, NOMBRE),
                seeThat("El trabajo", LaCreacionDelUsuario.conTrabajo(), equalTo(trabajo))
                        .orComplainWith(CreaUsuarioException.class, TRABAJO),
                seeThat("la fecha", LaCreacionDelUsuario.enLaFecha(), containsString(getFechaDelSistema()))
                        .orComplainWith(CreaUsuarioException.class, FECHA)
        );
    }

    @Cuando("el usuario inicie sesion erroneamente")
    public void elUsuarioInicieSesionErroneamente(List<IniciaSesionModel> datos) {
        theActorInTheSpotlight()
                .attemptsTo(IniciaSesion
                        .conLosDatos(email(datos.get(0).getEmail())
                                .yClave(LeeUnExcel.as(theActorInTheSpotlight()).getClave(datos.get(0).getEmail()))));
    }

    @Entonces("el usuario vera el mensaje de error {string}")
    public void elUsuarioVeraElMensajeDeError(String mensajeError) {
        theActorInTheSpotlight().should(
                seeThatResponse(MENSAJE_RESPUESTA,
                        response -> response
                                .statusCode(valueOf(mensajeError).getCodigo())
                                .body(ERROR, equalTo(valueOf(mensajeError).getMensaje())))
                        .orComplainWith(CreaUsuarioException.class, CODIGO_RESPUESTA));
    }

}

