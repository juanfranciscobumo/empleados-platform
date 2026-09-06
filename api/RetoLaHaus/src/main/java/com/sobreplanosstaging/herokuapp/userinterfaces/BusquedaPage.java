package com.sobreplanosstaging.herokuapp.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class BusquedaPage {
    private BusquedaPage(){

    }

    public static final Target INPUT_CIUDAD = Target.the("Campo ingresar ciudad")
            .located(By.cssSelector("input[placeholder='Busca por ciudad, zona o barrio']"));

    public static final Target LISTA_ZONAS = Target.the("Lista de zonas")
            .locatedBy("//div[@id='live-search']/ul/li/div[2][contains(text(),'{0}')]");

    public static final Target LISTA_TIPO_PROPIEDAD = Target.the("Lista tipo de propiedad")
            .locatedBy("//li[@data-value='{0}']");

    public static final Target BTN_INCREMENTAR_HABITACIONES = Target.the("Botón incrementar habitaciones")
            .locatedBy("//div[@id='rooms-number-field-container']/div/div/div[2]/a[2]");

    public static final Target TXT_HABITACIONES = Target.the("Texto número de habitaciones")
            .locatedBy("//div[@id='rooms-number-field-container']/div/div/div[2]/div");

    public static final Target BTN_BUSCAR = Target.the("Botón buscar")
            .located(By.cssSelector("button[data-lh-id='home-button-search']"));

}
