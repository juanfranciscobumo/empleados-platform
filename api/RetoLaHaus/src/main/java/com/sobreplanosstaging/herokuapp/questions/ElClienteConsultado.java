package com.sobreplanosstaging.herokuapp.questions;

import com.sobreplanosstaging.herokuapp.models.back.response.ConsultaUsuariosResponse;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

@Subject("Se devuelve el código del usuario")
public class ElClienteConsultado implements Question<Integer> {

    @Override
    public Integer answeredBy(Actor actor) {
        return lastResponse().jsonPath().getObject("", ConsultaUsuariosResponse.class).getData().getId();
    }

    public static ElClienteConsultado es() {
        return new ElClienteConsultado();
    }
}
