package co.com.whatsapp.interactions;

import io.appium.java_client.AppiumBy;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class ScrollTo {

    public static Performable android(String contact) {
        return Task.where("{0} realiza el scroll hasta " + contact,
                actor -> {
                    BrowseTheWeb.as(actor).getDriver().findElement(AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                                    ".scrollIntoView(new UiSelector()" +
                                    ".textMatches(\"" + contact + "\").instance(0))"));
                });
    }
}
