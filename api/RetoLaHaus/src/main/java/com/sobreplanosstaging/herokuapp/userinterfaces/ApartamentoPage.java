package com.sobreplanosstaging.herokuapp.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

public class ApartamentoPage {
    private ApartamentoPage() {

    }

    public static final Target TXT_HABITACIONES = Target.the("Texto número de habitaciones")
            .locatedBy("//p[contains(text(),'Habitaciones')]/following-sibling::p");

    public static final Target TXT_BANNOS = Target.the("Texto número de baños")
            .locatedBy("//p[contains(text(),'Baños')]/following-sibling::p");

    public static final Target TXT_PARQUEADEROS = Target.the("Texto número de parqueaderos")
            .locatedBy("/html/body/main/div/div[10]/div/div[1]/section[1]/div[4]/p");

    public static final Target TXT_PROYECTO = Target.the("Texto nombre del proyecto")
            .locatedBy("//*[@id='content-description']/div[2]/h1");

}
