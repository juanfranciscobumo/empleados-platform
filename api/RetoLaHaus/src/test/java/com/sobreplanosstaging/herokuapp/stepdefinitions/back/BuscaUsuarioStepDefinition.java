package com.sobreplanosstaging.herokuapp.stepdefinitions.back;

import com.sobreplanosstaging.herokuapp.tasks.back.ConsultaElUsuario;
import com.sobreplanosstaging.herokuapp.utils.constantes.Constantes;
import com.sobreplanosstaging.herokuapp.utils.enums.TagsEnum;
import com.sobreplanosstaging.herokuapp.exceptions.Exceptions;
import com.sobreplanosstaging.herokuapp.questions.ElClienteConsultado;
import com.sobreplanosstaging.herokuapp.utils.enums.RespuestasEnum;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static com.sobreplanosstaging.herokuapp.exceptions.Exceptions.CODIGO_DE_RESPUESTA;
import static com.sobreplanosstaging.herokuapp.exceptions.Exceptions.CODIGO_USUARIO;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class BuscaUsuarioStepDefinition {
    @Dado("que se conoce el endpoint del servicio")
    public void queSeConoceElEndpointDelServicio() {
        theActorInTheSpotlight().whoCan(CallAnApi.at(Constantes.ENDPOINT_REGRES));
    }

    @Cuando("se ingrese el código del usuario a consultar {string}")
    public void seIngreseElCodigoDelUsuarioAConsultar(String usuario) {
        theActorInTheSpotlight().attemptsTo(ConsultaElUsuario.enRegres(usuario));
    }

    @Entonces("se debe mostrar el usuario {int} y la {string}")
    public void seDebeMostrarElUsuarioYLa(Integer codigo, String respuesta) {
        theActorInTheSpotlight().should(seeThatResponse(TagsEnum.CODIGO_RESPUESTA.getAtributo(), response ->
                        response.statusCode(RespuestasEnum.valueOf(respuesta).getCodigo()))
                        .orComplainWith(Exceptions.class, CODIGO_DE_RESPUESTA),
                seeThat(ElClienteConsultado.es(), equalTo(codigo))
                        .orComplainWith(Exceptions.class, CODIGO_USUARIO));
    }
}
