package com.sobreplanosstaging.herokuapp.tasks.back;

import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Put;

import static com.sobreplanosstaging.herokuapp.utils.constantes.Constantes.RECURSO_BUSCAR_USUARIO;
import static io.restassured.http.ContentType.JSON;

public class ActualizaElUsuario implements Task {
    private CreacionUsuarioRequest usuario;

    public ActualizaElUsuario(CreacionUsuarioRequest usuario) {
        this.usuario = usuario;
    }

    @Step("Se hace la actualización del usuario")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Put.to(String.format(RECURSO_BUSCAR_USUARIO, usuario.getId()))
                .with(request -> request
                        .contentType(JSON)
                        .body(usuario)));

    }

    public static ActualizaElUsuario conLosDatos(CreacionUsuarioRequest usuario) {
        return Tasks.instrumented(ActualizaElUsuario.class, usuario);
    }
}
