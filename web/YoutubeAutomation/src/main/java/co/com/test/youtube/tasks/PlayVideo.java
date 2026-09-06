package co.com.test.youtube.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

import static co.com.test.youtube.pages.LookForAVideoPages.SKIPADDS;
import static co.com.test.youtube.pages.LookForAVideoPages.VIDEOSEARCHED;

public class PlayVideo implements Task {
    private String autorName;

    public PlayVideo(String autorName) {
        this.autorName = autorName;
    }

    @Override
    @Step("Play the video of the #autorName")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(VIDEOSEARCHED.of(autorName)),
                Click.on(SKIPADDS));
    }

    public static PlayVideo ofAutorName(String autorName) {
        return Tasks.instrumented(PlayVideo.class, autorName);
    }
}