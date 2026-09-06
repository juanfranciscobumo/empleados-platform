package co.com.whatsapp.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class PageWhatsapp {

    public static final Target BTN_NEW_CHAT = Target.the("BUTTON NEW CHAT")
            .locatedForAndroid(By.xpath("//*[@content-desc='Nuevo chat'] | //*[@name='Nuevo chat']"))
            .locatedForIOS(By.xpath("//*[@name='\u200ENuevo chat']"));


    public static final Target BTN_BUSCAR = Target.the("BUTTON SEARCH")
            .locatedForAndroid(By.xpath("//android.widget.TextView[@content-desc='Buscar']"))
            .locatedForIOS(By.xpath("//XCUIElementTypeSearchField[@name=\"\u200EBuscar\"]"));

    public static final Target INPUT_BUSCAR = Target.the("INPUT SEARCH")
            .locatedForAndroid(By.xpath("//*[@resource-id='com.whatsapp:id/search_src_text']"))
            .locatedForIOS(By.xpath("//XCUIElementTypeSearchField[@name=\"\u200EBuscar\"]"));


    public static final Target BTN_CONTACT = Target.the("BUTTON LOOK CONTACT")
            .locatedBy("//*[@text='{0}'] | (//XCUIElementTypeStaticText[@name='{0}'])[1]");


    public static final Target LBL_MESSAGE = Target.the("LABEL MESSAGE")
            .locatedForAndroid(By.xpath("//*[@class='android.widget.EditText']"))
            .locatedForIOS(By.xpath("//XCUIElementTypeTextView[@name=\"\u200EEscribir mensaje\"]"));

    public static final Target BTN_SEND = Target.the("BUTTON SEND MESSAGE")
            .locatedForAndroid(By.xpath("//*[@resource-id='com.whatsapp:id/send']"))
            .locatedForIOS(By.xpath("//XCUIElementTypeButton[@name=\"\u200EEnviar\"]"));


    public static final Target LBL_CONTAC = Target.the("LABEL CONTACT")
            .locatedForAndroid(By.xpath("//*[@resource-id='com.whatsapp:id/conversation_contact_name']"))
            .locatedForIOS(By.xpath("(//XCUIElementTypeButton[@name=\"Karen Estupinan\"])[2]"));


    public static final Target LBL_MESSAGE_SEND = Target.the("LABEL SENT MESSAGE").locatedBy("//*[@text='{0}'] | //XCUIElementTypeOther[@name=\"\u200EMi Mensaje, {0}, 5:36p.m., \u200EEntregado al destinatario\"]");

}
