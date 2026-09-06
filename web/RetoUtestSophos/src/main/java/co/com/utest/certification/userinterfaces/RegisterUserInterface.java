package co.com.utest.certification.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;


import static co.com.utest.certification.utils.Constantes.URL;

public class RegisterUserInterface {

    public static final Target BTNBECOMEUTEST = Target.the("Button Init register")
            .locatedBy("//div[@class='col-sm-6 col-xs-12 center-text']/a");

    public static final Target LBLFIRSTNAME = Target.the("label first name")
            .located(By.id("firstName"));

    public static final Target LBLLASTNAME = Target.the("label last name")
            .located(By.id("lastName"));

    public static final Target LBLEMAIL = Target.the("label email address")
            .located(By.id("email"));

    public static final Target LBLMONTH = Target.the("label month")
            .located(By.id("birthMonth"));

    public static final Target LBLDAY = Target.the("label day")
            .located(By.id("birthDay"));

    public static final Target LBLYEAR = Target.the("label year")
            .located(By.id("birthYear"));

    public static final Target LBLLANGUAGE = Target.the("label language")
            .locatedBy("//input[@type='search']");

    public static final Target LISTLANGUAGE = Target.the("list languages")
            .locatedBy("//ul[@class='ui-select-choices ui-select-choices-content ui-select-dropdown dropdown-menu']/li/div/span/div[.='{0}']");

    public static final Target BTNNEXTLOCATION = Target.the("button next location")
            .locatedBy("//span[.='Next: Location']");

    public static final Target BTNNEXTDEVICE = Target.the("button next device")
            .locatedBy("//span[.='Next: Devices']");

    public static final Target LBLDEVICE = Target.the("label device")
            .locatedBy("//div[@id='mobile-device']/div[1]/div[2]/div");

    public static final Target LBLMODEL = Target.the("label model")
            .locatedBy("//div[@id='mobile-device']/div[2]/div[2]/div");

    public static final Target LBLOPERATIVESYSTEM = Target.the("label operative system")
            .locatedBy("//div[@id='mobile-device']/div[3]/div[2]/div");

    public static final Target BTNLASTSTEP = Target.the("button next last step")
            .locatedBy("//span[.='Next: Last Step']");

    public static final Target LBLPASSWORD = Target.the("label password")
            .located(By.id("password"));

    public static final Target LBLCONFIRMPASSWORD = Target.the("label confirm password")
            .located(By.id("confirmPassword"));

    public static final Target CHTERMS = Target.the("check terms")
            .locatedBy("//form[@name='userForm']/div[5]/label/span[1]");

    public static final Target CHPRIVACITY = Target.the("check privacity")
            .locatedBy("//form[@name='userForm']/div[6]/label/span[1]");

    public static final Target BTNCOMPLETESETUP = Target.the("button complete setup")
            .locatedBy("//span[.='Complete Setup']");
}
