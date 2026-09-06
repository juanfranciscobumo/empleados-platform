package com.sobreplanosstaging.herokuapp.tasks.back;

import com.sobreplanosstaging.herokuapp.utils.constantes.Constantes;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static io.restassured.http.ContentType.JSON;

public class BorraElUsuario implements Task {
    private String usuario;

    public BorraElUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Step("Se hace el borrado del usuario #usuario")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Delete.from(String.format(Constantes.RECURSO_BUSCAR_USUARIO, usuario))
                .with(request -> request.contentType(JSON)));
    }

    public static BorraElUsuario enRegres(String usuario) {
        return Tasks.instrumented(BorraElUsuario.class, usuario);
    }
}
