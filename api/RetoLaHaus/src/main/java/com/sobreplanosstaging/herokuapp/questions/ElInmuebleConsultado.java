package com.sobreplanosstaging.herokuapp.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;

import static com.sobreplanosstaging.herokuapp.userinterfaces.ApartamentoPage.*;

public class ElInmuebleConsultado {
    private ElInmuebleConsultado() {

    }

    public static Question<String> conHabitacionesEs() {
        return actor -> actor.asksFor(Text.of(TXT_HABITACIONES).asString());
    }

    public static Question<String> conParqueaderosEs() {
        return actor -> actor.asksFor(Text.of(TXT_PARQUEADEROS).asString());
    }

    public static Question<String> conBannosEs() {
        return actor -> actor.asksFor(Text.of(TXT_BANNOS).asString());
    }

    public static Question<String> conNombre() {
        return actor -> actor.asksFor(Text.of(TXT_PROYECTO).asString());
    }
}
