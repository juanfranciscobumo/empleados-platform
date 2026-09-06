package com.qvision.certificacion.userinterfaces;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.qvision.certificacion.utils.PrecioMasBajo.obtenerPrecioMasBajo;

public class PreciosHotelPages extends PageObject {

    @FindBy(xpath = "//div[@class='current-hotels']/div/div/div[2]/div[1]")
    public static List<WebElement> ltPrecios;
    public static String precio = "//div[@class='current-hotels']/div/div/div[2]/div[1]/p[contains(text(),'%s')]/parent::div/following-sibling::div[2]";

    public void buscarPrecioMasBajo()  {
        find(By.xpath(String.format(precio, obtenerPrecioMasBajo()))).click();
    }

}
