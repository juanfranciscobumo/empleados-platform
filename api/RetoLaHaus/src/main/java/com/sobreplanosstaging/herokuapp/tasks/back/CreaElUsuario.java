package com.sobreplanosstaging.herokuapp.tasks.back;

import com.sobreplanosstaging.herokuapp.models.back.request.CreacionUsuarioRequest;
import com.sobreplanosstaging.herokuapp.utils.constantes.Constantes;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static io.restassured.http.ContentType.JSON;

public class CreaElUsuario implements Task {
    private CreacionUsuarioRequest usuario;

    public CreaElUsuario(CreacionUsuarioRequest usuario) {
        this.usuario = usuario;
    }

    @Step("Se crea el usuario")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.to(Constantes.RECURSO_CREAR_USUARIO)
                .with(request -> request.contentType(JSON)
                        .body(usuario)));
    }

    public static CreaElUsuario conLosDatos(CreacionUsuarioRequest usuario) {
        return Tasks.instrumented(CreaElUsuario.class, usuario);
    }
}
