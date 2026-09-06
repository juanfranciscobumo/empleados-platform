package com.sobreplanosstaging.herokuapp.tasks.back;

import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import static com.sobreplanosstaging.herokuapp.utils.constantes.Constantes.RECURSO_BUSCAR_USUARIO;
import static io.restassured.http.ContentType.JSON;

public class ActualizaElUsuarioConPatch implements Task {
    private CreacionUsuarioRequest usuario;

    public ActualizaElUsuarioConPatch(CreacionUsuarioRequest usuario) {
        this.usuario = usuario;
    }

    @Step("Se hace la actualización al usuario")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Patch.to(String.format(RECURSO_BUSCAR_USUARIO, usuario.getId()))
                .with(request -> request
                        .contentType(JSON)
                        .body(usuario)));
    }

    public static ActualizaElUsuarioConPatch conLosDatos(CreacionUsuarioRequest usuario) {
        return Tasks.instrumented(ActualizaElUsuarioConPatch.class, usuario);
    }
}
