package com.sobreplanosstaging.herokuapp.tasks.back;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static com.sobreplanosstaging.herokuapp.utils.constantes.Constantes.RECURSO_BUSCAR_USUARIO;
import static io.restassured.http.ContentType.JSON;

public class ConsultaElUsuario implements Task {
    private String usuario;

    public ConsultaElUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Step("Se hace la consulta del usuario #usuario")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Get.resource(String.format(RECURSO_BUSCAR_USUARIO, usuario))
                .with(request -> request.contentType(JSON)));
    }

    public static ConsultaElUsuario enRegres(String usuario) {
        return Tasks.instrumented(ConsultaElUsuario.class, usuario);
    }
}
