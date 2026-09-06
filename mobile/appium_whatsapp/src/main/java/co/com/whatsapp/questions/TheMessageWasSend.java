package co.com.whatsapp.questions;

import co.com.whatsapp.pages.PageWhatsapp;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class TheMessageWasSend {

    public static Question<String> withMessage(String message) {
        return actor -> Text.of(PageWhatsapp.LBL_MESSAGE_SEND.of(message)).answeredBy(actor);
    }

    public static Question<String> at() {
        return actor -> Text.of(PageWhatsapp.LBL_CONTAC).answeredBy(actor);
    }

}