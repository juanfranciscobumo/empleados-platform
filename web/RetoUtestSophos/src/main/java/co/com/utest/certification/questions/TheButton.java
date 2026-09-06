package co.com.utest.certification.questions;

import co.com.utest.certification.userinterfaces.RegisterUserInterface;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class TheButton implements Question<String> {
    @Override
    public String answeredBy(Actor actor) {
        return Text.of(RegisterUserInterface.BTNCOMPLETESETUP).answeredBy(actor);
    }

    public static TheButton is() {
        return new TheButton();
    }
}
