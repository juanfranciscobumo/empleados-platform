package co.com.whatsapp.tasks;


import co.com.whatsapp.interactions.ScrollTo;
import co.com.whatsapp.model.TestData;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static co.com.whatsapp.pages.PageWhatsapp.*;

public class SendMessage {

    public static Performable onWhatsapp(TestData data) {
        return Task.where(
                "{0} Busca el contacto " + data.getContact(),
                Click.on(BTN_NEW_CHAT),
                ScrollTo.android(data.getContact()),
                Click.on(BTN_CONTACT.of(data.getContact())),
                Enter.theValue(data.getMessage()).into(LBL_MESSAGE),
                Click.on(BTN_SEND));
    }
}