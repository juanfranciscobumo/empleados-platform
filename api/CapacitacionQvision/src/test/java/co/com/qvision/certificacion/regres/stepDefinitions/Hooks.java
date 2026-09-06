package co.com.qvision.certificacion.regres.stepDefinitions;

import co.com.qvision.certificacion.regres.abilities.LeeUnExcel;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class Hooks {
    private static final String ACTOR="Juan";
    @Before
    public void set_the_stage() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled(ACTOR).whoCan(LeeUnExcel.paraVerLosDatos());
    }

}
