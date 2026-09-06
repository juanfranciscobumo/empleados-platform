package co.com.test.youtube.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.questions.Text;

import static co.com.test.youtube.pages.LookAtTheMenuPage.TEXT_LIVE;

@Subject("Video watched")
public class TheMenuWasWatched implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(TEXT_LIVE).answeredBy(actor);
    }

    public static TheMenuWasWatched inYouTube() {
        return new TheMenuWasWatched();
    }
}
