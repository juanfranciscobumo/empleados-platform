package co.com.test.youtube.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;

import static co.com.test.youtube.pages.LookForAVideoPages.MUSICPLAYER;

@Subject("Video time")
public class VideoWasPlayed implements Question<String> {
    private String timeVideo;

    public VideoWasPlayed(String timeVideo) {
        this.timeVideo = timeVideo;
    }

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(MUSICPLAYER.of(timeVideo)).answeredBy(actor);
    }

    public static VideoWasPlayed inYouTube(String timeVideo) {
        return new VideoWasPlayed(timeVideo);
    }
}
