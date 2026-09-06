package com.sobreplanosstaging.herokuapp.stepdefinitions.back;

import com.sobreplanosstaging.herokuapp.tasks.back.ActualizaElUsuario;
import com.sobreplanosstaging.herokuapp.builders.CreacionUsuarioBuilder;
import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;
import io.cucumber.java.es.Cuando;

import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class ActualizaUsuarioStepDefinition {
    @Cuando("se ingresen los datos del usuario a actualizar")
    public void seIngresenLosDatosDelUsuarioAActualizar(List<CreacionUsuarioRequest> datos) {
        theActorInTheSpotlight().attemptsTo(ActualizaElUsuario.conLosDatos(CreacionUsuarioBuilder
                .name(datos.get(0).getName())
                .id(datos.get(0).getId())
                .job(datos.get(0).getJob())));
    }

}
