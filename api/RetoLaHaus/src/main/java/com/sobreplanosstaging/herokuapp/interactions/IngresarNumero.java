package com.sobreplanosstaging.herokuapp.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;


public class IngresarNumero implements Interaction {

    private String unidades;
    private Target targetButton;
    private Target targetTexto;

    public IngresarNumero(String unidades, Target targetButton, Target targetTexto) {
        this.unidades = unidades;
        this.targetButton = targetButton;
        this.targetTexto = targetTexto;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        while (true) {
            if (!targetTexto.resolveFor(actor).getText().equals(unidades)) {
                actor.attemptsTo(Click.on(targetButton));
            } else {
                break;
            }
        }
    }

    public static IngresarNumero deUnidades(String unidades, Target targetButton, Target targetTexto) {
        return Tasks.instrumented(IngresarNumero.class, unidades, targetButton, targetTexto);
    }
}
