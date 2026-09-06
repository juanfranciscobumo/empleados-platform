package co.com.test.youtube.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Scroll;

import static co.com.test.youtube.pages.LookAtTheMenuPage.LIVE;

public class PlayVideoInLive implements Task {

    @Override
    @Step("Look at the menu on live")
    public <T extends Actor> void performAs(T actor) {
                actor.attemptsTo(
                        Scroll.to(LIVE),
                        Click.on(LIVE));
    }

    public static PlayVideoInLive onYouTube() {
        return Tasks.instrumented(PlayVideoInLive.class);
    }
}