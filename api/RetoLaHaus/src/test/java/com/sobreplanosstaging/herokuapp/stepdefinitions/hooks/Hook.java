package com.sobreplanosstaging.herokuapp.stepdefinitions.hooks;

import com.sobreplanosstaging.herokuapp.utils.constantes.Constantes;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class Hook {
    @Before()
    public void iniciarActor() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActor(Constantes.ACTOR);
    }
    @After
    public void cerrarNavegador(){
        BrowseTheWeb.as(theActorInTheSpotlight()).getDriver().quit();
    }
}
