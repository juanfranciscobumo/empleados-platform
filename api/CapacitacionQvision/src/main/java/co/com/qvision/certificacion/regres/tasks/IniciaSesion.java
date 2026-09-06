package co.com.qvision.certificacion.regres.tasks;

import co.com.qvision.certificacion.regres.interactions.Post;
import co.com.qvision.certificacion.regres.models.IniciaSesionModel;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import static co.com.qvision.certificacion.regres.utils.Constantes.RECURSO_INICIAR_SESION;
import static io.restassured.http.ContentType.JSON;

public class IniciaSesion implements Task {

    private final IniciaSesionModel iniciarSesionModel;

    public IniciaSesion(IniciaSesionModel iniciarSesionModel) {
        this.iniciarSesionModel = iniciarSesionModel;
    }

    @Step("{0} inicia sesion en regress")
    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(Post.to(RECURSO_INICIAR_SESION)
                .with(request -> request.contentType(JSON)
                        .body(iniciarSesionModel)
                        .relaxedHTTPSValidation()
                        .urlEncodingEnabled(false)));
    }

    public static IniciaSesion conLosDatos(IniciaSesionModel iniciarSesionModel) {
        return Tasks.instrumented(IniciaSesion.class, iniciarSesionModel);

    }
}
