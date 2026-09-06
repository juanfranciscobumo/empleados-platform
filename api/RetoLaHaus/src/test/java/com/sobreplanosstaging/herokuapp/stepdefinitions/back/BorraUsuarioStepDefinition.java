package com.sobreplanosstaging.herokuapp.stepdefinitions.back;

import com.sobreplanosstaging.herokuapp.tasks.back.BorraElUsuario;
import com.sobreplanosstaging.herokuapp.utils.enums.RespuestasEnum;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static com.sobreplanosstaging.herokuapp.utils.enums.TagsEnum.CODIGO_RESPUESTA;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

public class BorraUsuarioStepDefinition {
    @Cuando("se ingrese el código del usuario a borrar {string}")
    public void seIngreseElCodigoDelUsuarioABorrar(String usuario) {
        theActorInTheSpotlight().attemptsTo(BorraElUsuario.enRegres(usuario));
    }

    @Entonces("no se debe visualizar el usuario {string}")
    public void noSeDebeVisualizarElUsuario(String respuesta) {
        theActorInTheSpotlight().should(seeThatResponse(CODIGO_RESPUESTA.getAtributo(), response ->
                response.statusCode(RespuestasEnum.valueOf(respuesta).getCodigo())));
    }
}
