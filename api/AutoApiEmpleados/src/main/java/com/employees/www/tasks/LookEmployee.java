package com.employees.www.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static com.employees.www.utils.Constants.RECURSO;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class LookEmployee implements Task {
    private int id;

    public LookEmployee(int id) {
        this.id = id;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource(String.format(RECURSO, id)));
    }

    public static LookEmployee byId(int id) {
        return instrumented(LookEmployee.class, id);
    }
}
