package co.com.test.youtube.stepdefinitions;

import co.com.test.youtube.exceptions.YouTubeError;
import co.com.test.youtube.questions.TheMenuWasWatched;
import co.com.test.youtube.tasks.PlayVideoInLive;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static co.com.test.youtube.exceptions.YouTubeError.THE_TEXT_WAS_NOT_FOUND;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class LookAtTheMenuStepDefinition {

    @When("Juan look the menu")
    public void juanLookTheMenu() {
        theActorInTheSpotlight().attemptsTo(PlayVideoInLive.onYouTube());
    }

    @Then("Juan could watch the menu {string}")
    public void juanCouldWatchTheMenu(String live) {
        theActorInTheSpotlight().should(seeThat(TheMenuWasWatched.inYouTube(), equalTo(live))
                .orComplainWith(YouTubeError.class, THE_TEXT_WAS_NOT_FOUND));
    }
}
