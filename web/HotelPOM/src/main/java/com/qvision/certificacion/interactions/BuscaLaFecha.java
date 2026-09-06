package com.qvision.certificacion.interactions;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BuscaLaFecha extends PageObject {
    private WebElement mesInicial;
    private WebElement btnNext;
    private List<WebElement> ltDias;
    private String mes;
    private String ano;
    private String dia;
    private boolean a = true;

    public BuscaLaFecha(WebElement mesInicial, WebElement btnNext, List<WebElement> ltDias, String mes, String ano, String dia) {
        this.mesInicial = mesInicial;
        this.btnNext = btnNext;
        this.ltDias = ltDias;
        this.mes = mes;
        this.ano = ano;
        this.dia = dia;
    }

    public void action() {
        waitABit(3000);
        while (true) {
            if (getMes(mesInicial.getText(), mes, ano)) {
                waitABit(3000);
                darClickOnLista(ltDias, dia);
                break;
            } else {
                btnNext.click();
            }
        }
    }

    public static void paraSeleccionarla(WebElement mesInicial, WebElement btnNext, List<WebElement> ltDias, String mes, String ano, String dia) {
        new BuscaLaFecha(mesInicial, btnNext, ltDias, mes, ano, dia).action();
    }

    public static void darClickOnLista(List<WebElement> ltDias, String dia) {
        ltDias.stream()
                .filter(p -> p.getText().equals(dia))
                .forEach(WebElement::click);
    }

    public static boolean getMes(String mesInicial, String mes, String ano) {
        return mesInicial.contains(mes) && mesInicial.contains(ano);
    }
}
