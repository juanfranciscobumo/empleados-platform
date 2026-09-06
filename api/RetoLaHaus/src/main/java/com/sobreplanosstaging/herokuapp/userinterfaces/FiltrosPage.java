package com.sobreplanosstaging.herokuapp.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class FiltrosPage {
    private FiltrosPage() {

    }

    public static final Target BTN_INCREMENTAR_BANNOS = Target.the("Botón incrementar baños")
            .located(By.id("counter-increment-bathrooms"));

    public static final Target TXT_NUMERO_BANNOS = Target.the("Texto número de baños")
            .locatedBy("//i[@id='counter-increment-bathrooms']/parent::button/preceding-sibling::span");

    public static final Target BARRA_AREA_MINIMA = Target.the("Barra area mínima")
            .located(By.cssSelector("span[data-lh-id='filter-panel-desktop-area-slider-min-handler']"));

    public static final Target BARRA_AREA_MAXIMA = Target.the("Barra area máxima")
            .located(By.cssSelector("span[data-lh-id='filter-panel-desktop-area-slider-max-handler']"));
    public static final Target INPUT_AREA_MINIMA = Target.the("Input area mínima")
            .located(By.id("area-slider-min"));

    public static final Target INPUT_AREA_MAXIMA = Target.the("Input area máxima")
            .located(By.id("area-slider-max"));


    public static final Target APARTAMENTO = Target.the("Total de registros por página")
            .locatedBy("//div[@class='client-cards']/article//h2[contains(text(),'{0}')]/parent::*");

}
