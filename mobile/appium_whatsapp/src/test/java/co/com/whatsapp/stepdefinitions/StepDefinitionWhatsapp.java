package co.com.whatsapp.stepdefinitions;

import static co.com.whatsapp.builders.DatosBuilder.contact;
import static co.com.whatsapp.exceptions.WhatsappException.CONTACT_NO_FOUND;
import static co.com.whatsapp.exceptions.WhatsappException.MESSAGE_NO_FOUND;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

import co.com.whatsapp.exceptions.WhatsappException;
import co.com.whatsapp.questions.TheMessageWasSend;
import co.com.whatsapp.tasks.SendMessage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;

public class StepDefinitionWhatsapp {

    @Managed
    AndroidDriver driver;

    @Before
    public void set_the_stage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("^that (.*) is on whatsapp$")
    public void thatJuanIsOnWhatsapp(String juan) {
        theActorCalled(juan).whoCan(BrowseTheWeb.with(driver));
    }

    @When("^look the personal contact (.*) and send the message (.*)$")
    public void lookThePersonalContactMyNenaAndSendTheMessageEstoEsUnMensajeDePrueba(String contact, String message) {
        theActorInTheSpotlight().attemptsTo(SendMessage.onWhatsapp(contact(contact).andSendMessage(message)));
    }

    @Then("^juan will check that message was send at contact (.*) with message (.*)$")
    public void juanWillCheckThatMessageWasSendAtContactMyNenaWithMessageDisculpeSeorita(String contact, String message) {
        theActorInTheSpotlight().should(
                seeThat("El contacto", TheMessageWasSend.at(), equalTo(contact)).orComplainWith(WhatsappException.class, CONTACT_NO_FOUND),
                seeThat("El mensaje", TheMessageWasSend.withMessage(message), equalTo(message)).orComplainWith(WhatsappException.class, MESSAGE_NO_FOUND));

    }
}
