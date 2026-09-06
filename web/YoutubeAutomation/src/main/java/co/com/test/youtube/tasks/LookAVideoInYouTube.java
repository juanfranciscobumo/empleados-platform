package co.com.test.youtube.tasks;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.ui.*;

import static co.com.test.youtube.pages.LookForAVideoPages.*;

public class LookAVideoInYouTube implements Task {
    private String videoName;

    public LookAVideoInYouTube(String videoName) {
        this.videoName = videoName;
    }

    @Override
    @Step("Look for the video with name #videoName")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(videoName).into(InputField.withNameOrId(LBLSEARCH)),
                Click.on(Button.withNameOrId(BTNSEARCH)));
    }

    public static LookAVideoInYouTube withName(String videoName) {
        return Tasks.instrumented(LookAVideoInYouTube.class, videoName);
    }
}