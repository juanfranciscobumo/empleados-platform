package com.qvision.certificacion.userinterfaces;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.qvision.certificacion.utils.Constantes.URL;

@DefaultUrl(URL)
public class AgenciaDeViajesPages extends PageObject {

    @FindBy(xpath = "//input[@name='location']/following-sibling::div")
    public static WebElement selUbicacion;

    @FindBy(xpath = "//input[@name='checkIn']//following-sibling::div/div[2]")
    public static WebElement calendario;

    @FindBy(xpath = "//div[@class='dx-calendar-views-wrapper']/div[1]/table//td/span")
    public static List<WebElement> ltDias;

    @FindBy(xpath = "//div[@class='dx-popup-content']/div/div[2]/a[3]/div/i")
    public static WebElement btnNext;

    @FindBy(xpath = "//div[@class='dx-overlay-content dx-popup-normal dx-resizable']/div//div/div[2]/a[3]/div/i")
    public static WebElement btnNextDos;

    @FindBy(xpath = "//*[@class='dx-popup-content']/div/div[2]/a[2]")
    public static WebElement btnmesinicial;

    @FindBy(xpath = "//input[@name='checkOut']//following-sibling::div/div[2]")
    public static WebElement calendarioDos;

    @FindBy(xpath = "/html/body/div/div[2]/div/div/div/div/div/div[1]//td")
    public static List<WebElement> ltDiasDos;

    @FindBy(xpath = "/html/body/div/div[2]/div/div/div/div[2]/a[2]")
    public static WebElement btnmesinicialdos;

    @FindBy(xpath = "//input[@name='rooms']/following-sibling::div")
    public static WebElement ltRoom;

    @FindBy(xpath = "//input[@name='adults']/following-sibling::div")
    public static WebElement ltAdults;

    @FindBy(xpath = "//input[@name='children']/following-sibling::div")
    public static WebElement ltChildren;

    @FindBy(xpath = "//span[.='SEARCH']")
    public static WebElement btnSearch;

    public static String listaUbicacion = "//*[@class='dx-scrollview-content']/div/div[contains(text(),'%s')]";
    public static String room = "//div[@class='dx-item-content dx-list-item-content'][contains(text(),'%s')]";
    public static String adultos = "/html/body/div/div[2]/div/div/div/div[1]/div/div[1]/div[2]/div/div[contains(text(),'%s')]";
    public static String children = "//div[.='%s']";

    public void abrirUbicaciones(String ubicacion) {
        find(By.xpath(String.format(listaUbicacion, ubicacion))).click();
    }

    public void seleccionarRooms(String cantidadHabitaciones) {
        find(By.xpath(String.format(room, cantidadHabitaciones))).click();
    }

    public void seleccionarAdultos(String cantidadAdultos) {
        find(By.xpath(String.format(adultos, cantidadAdultos))).click();
    }

    public void seleccionarChildren(String cantidadChildren) {
        find(By.xpath(String.format(children, cantidadChildren))).click();
    }
}
