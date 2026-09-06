package com.sobreplanosstaging.herokuapp.stepdefinitions.back;

import com.sobreplanosstaging.herokuapp.builders.CreacionUsuarioBuilder;
import com.sobreplanosstaging.herokuapp.exceptions.Exceptions;
import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;
import com.sobreplanosstaging.herokuapp.tasks.back.CreaElUsuario;
import com.sobreplanosstaging.herokuapp.utils.enums.RespuestasEnum;
import com.sobreplanosstaging.herokuapp.utils.enums.TagsEnum;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static com.sobreplanosstaging.herokuapp.exceptions.Exceptions.CREACION_DE_USUARIO;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class CreaUsuarioStepDefinition {
    @Cuando("se ingresen los datos del usuario a crear")
    public void seIngresenLosDatosDelUsuarioACrear(List<CreacionUsuarioRequest> datos) {
        theActorInTheSpotlight().attemptsTo(CreaElUsuario.conLosDatos(CreacionUsuarioBuilder
                .name(datos.get(0).getName())
                .job(datos.get(0).getJob())));
    }

    @Entonces("se debe ver el usuario {string} y el job {string} con {string}")
    public void seDebeVerElUsuarioYElJobCon(String name, String job, String respuesta) {
        theActorInTheSpotlight().should(TagsEnum.CODIGO_RESPUESTA.getAtributo(),
                seeThatResponse(response -> response.statusCode(RespuestasEnum.valueOf(respuesta).getCodigo())
                        .body(TagsEnum.NAME.getAtributo(), equalTo(name))
                        .body(TagsEnum.JOB.getAtributo(), equalTo(job)))
                        .orComplainWith(Exceptions.class, CREACION_DE_USUARIO));
    }

}
