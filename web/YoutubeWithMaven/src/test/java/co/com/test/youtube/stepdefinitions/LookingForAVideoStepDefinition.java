package co.com.test.youtube.stepdefinitions;

import co.com.test.youtube.exceptions.YouTubeError;
import co.com.test.youtube.pages.LookForAVideoPages;
import co.com.test.youtube.questions.VideoWasPlayed;
import co.com.test.youtube.tasks.LookAVideoInYouTube;
import co.com.test.youtube.tasks.PlayVideo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import org.openqa.selenium.WebDriver;

import static co.com.test.youtube.exceptions.YouTubeError.NOT_FOUND_VIDEO;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class LookingForAVideoStepDefinition {

    WebDriver driver;

    @Given("that {string} is on YouTube")
    public void thatIsOnYouTube(String actor) {
        theActorCalled(actor).whoCan(BrowseTheWeb.with(driver))
                .wasAbleTo(Open.browserOn().the(LookForAVideoPages.class));
    }

    @When("Juan looks for the song {string}")
    public void juanLooksForTheSong(String videoName) {
        theActorInTheSpotlight().attemptsTo(LookAVideoInYouTube.withName(videoName));
    }

    @When("Juan plays video of {string}")
    public void juanPlaysVideoOf(String autorName) {
        theActorInTheSpotlight().attemptsTo(PlayVideo.ofAutorName(autorName));
    }

    @Then("Juan checks that the video has duration of {string}")
    public void juanChecksThatTheVideoHasDurationOf(String timeVideo) {
        theActorInTheSpotlight().should(seeThat(VideoWasPlayed.inYouTube(timeVideo), equalTo(timeVideo))
                .orComplainWith(YouTubeError.class, NOT_FOUND_VIDEO));
    }
}
