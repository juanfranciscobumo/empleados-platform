package co.com.utest.certification.tasks;

import co.com.utest.certification.interfaces.IRegisterUser;
import co.com.utest.certification.models.RegisterUserModel;
import co.com.utest.certification.userinterfaces.RegisterUserInterface;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Hit;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.actions.selectactions.SelectByIndexFromBy;
import net.serenitybdd.screenplay.actions.selectactions.SelectByIndexFromElement;
import org.openqa.selenium.Keys;

public class RegisterUser implements Task {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String month;
    private String year;
    private String day;
    private String languages;
    private String device;
    private String model;
    private String operatingSystem;
    private String password;

    public RegisterUser(RegisterUserModel datos) {
        this.firstName = datos.getFirstName();
        this.lastName = datos.getLastName();
        this.emailAddress = datos.getEmailAddress();
        this.month = datos.getMonth();
        this.year = datos.getYear();
        this.day = datos.getDay();
        this.languages = datos.getLanguages();
        this.device = datos.getDevice();
        this.model = datos.getModel();
        this.operatingSystem = datos.getOperatingSystem();
        this.password = datos.getPassword();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(RegisterUserInterface.BTNBECOMEUTEST),
                Enter.theValue(firstName).into(RegisterUserInterface.LBLFIRSTNAME),
                Enter.theValue(lastName).into(RegisterUserInterface.LBLLASTNAME),
                Enter.theValue(emailAddress).into(RegisterUserInterface.LBLEMAIL),
                SelectFromOptions.byVisibleText(month).from(RegisterUserInterface.LBLMONTH),
                SelectFromOptions.byVisibleText(day).from(RegisterUserInterface.LBLDAY),
                SelectFromOptions.byVisibleText(year).from(RegisterUserInterface.LBLYEAR),
                Click.on(RegisterUserInterface.LBLLANGUAGE),
                Click.on(RegisterUserInterface.LISTLANGUAGE.of(languages)),
                Click.on(RegisterUserInterface.BTNNEXTLOCATION),
                Click.on(RegisterUserInterface.BTNNEXTDEVICE),
                Click.on(RegisterUserInterface.LBLDEVICE),
                Click.on(RegisterUserInterface.LISTLANGUAGE.of(device)),
                Click.on(RegisterUserInterface.LBLMODEL),
                Click.on(RegisterUserInterface.LISTLANGUAGE.of(model)),
                Click.on(RegisterUserInterface.LBLOPERATIVESYSTEM),
                Click.on(RegisterUserInterface.LISTLANGUAGE.of(operatingSystem)),
                Click.on(RegisterUserInterface.BTNLASTSTEP),
                Enter.theValue(password).into(RegisterUserInterface.LBLPASSWORD),
                Enter.theValue(password).into(RegisterUserInterface.LBLCONFIRMPASSWORD),
                Click.on(RegisterUserInterface.CHPRIVACITY),
                Click.on(RegisterUserInterface.CHTERMS)
        );
    }

    public static RegisterUser inUtest(RegisterUserModel data) {
        return Tasks.instrumented(RegisterUser.class, data);
    }
}
