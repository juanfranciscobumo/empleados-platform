package co.com.utest.certification.stepdefinitions;

import co.com.utest.certification.builders.RegisterUserBuilder;
import co.com.utest.certification.exceptions.ExceptionRegisterUser;
import co.com.utest.certification.models.RegisterUserModel;
import co.com.utest.certification.questions.TheButton;
import co.com.utest.certification.tasks.RegisterUser;
import co.com.utest.certification.userinterfaces.RegisterUserInterface;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static co.com.utest.certification.utils.Constantes.URL;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static co.com.utest.certification.exceptions.ExceptionRegisterUser.BUTTON;

public class RegisterUserStepDefinition {
    WebDriver driver;

    @Before
    public void initUser() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.aNewActor();
    }

    @DataTableType
    public RegisterUserModel cuenta(Map<String, String> datos) {
        RegisterUserModel register = new RegisterUserModel();
        register.setFirstName(datos.get("firstName"));
        register.setLastName(datos.get("lastName"));
        register.setEmailAddress(datos.get("emailAddress"));
        register.setMonth(datos.get("month"));
        register.setYear(datos.get("year"));
        register.setDay(datos.get("day"));
        register.setLanguages(datos.get("languages"));
        register.setDevice(datos.get("device"));
        register.setModel(datos.get("model"));
        register.setOperatingSystem(datos.get("operatingSystem"));
        register.setPassword(datos.get("password"));
        return register;
    }

    @Given("the user is the page utest")
    public void theUserIsThePageUtest() {
        theActorInTheSpotlight().whoCan(BrowseTheWeb.with(driver))
                .wasAbleTo(Open.url(URL));
    }

    @When("enter his datas")
    public void enterHisDatas(RegisterUserModel data) {
        theActorInTheSpotlight().attemptsTo(RegisterUser.inUtest(RegisterUserBuilder
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .emailAddress(data.getEmailAddress())
                .day(data.getDay())
                .month(data.getMonth())
                .year(data.getYear())
                .languages(data.getLanguages())
                .device(data.getDevice())
                .model(data.getModel())
                .operatingSystem(data.getOperatingSystem())
                .password(data.getPassword())));
    }

    @Then("the user will registered and he will find the button {string}")
    public void theUserWillRegisteredAndHeWillFindTheButton(String nameButton) {
        theActorInTheSpotlight().should(seeThat(TheButton.is(), equalTo(nameButton))
                .orComplainWith(ExceptionRegisterUser.class, BUTTON));
    }
}
