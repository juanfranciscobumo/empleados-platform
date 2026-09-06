package com.sobreplanosstaging.herokuapp.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.interactions.Actions;

public class MueveLaBarra implements Interaction {
    private String datoBuscado;
    private Target targetBarra;
    private Target targetInput;
    private int direccion;

    public MueveLaBarra(String datoBuscado, Target targetBarra, Target targetInput, int direccion) {
        this.datoBuscado = datoBuscado;
        this.targetBarra = targetBarra;
        this.targetInput = targetInput;
        this.direccion = direccion;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Scroll.to(targetInput));
        while (true) {
            if (targetInput.resolveFor(actor).getValue().equals(datoBuscado)) {
                break;
            } else {
                new Actions(BrowseTheWeb.as(actor).getDriver())
                        .dragAndDropBy(targetBarra.resolveFor(actor), direccion, 0)
                        .build()
                        .perform();
            }
        }
    }

    public static MueveLaBarra hacia(String datoBuscado, Target targetBarra, Target targetInput, int direccion) {
        return Tasks.instrumented(MueveLaBarra.class, datoBuscado, targetBarra, targetInput, direccion);
    }
}
