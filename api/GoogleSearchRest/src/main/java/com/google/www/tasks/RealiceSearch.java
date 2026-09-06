package com.google.www.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import java.util.Map;

import static com.google.www.utils.Constantes.RECURSO;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class RealiceSearch implements Task {
    private Map<String, String> data;

    public RealiceSearch(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource(RECURSO).with(request -> request.params(data)));
    }

    public static RealiceSearch inGoogle(Map<String, String> data) {
        return instrumented(RealiceSearch.class, data);
    }
}
