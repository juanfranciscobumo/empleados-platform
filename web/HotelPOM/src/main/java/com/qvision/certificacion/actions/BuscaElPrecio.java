package com.qvision.certificacion.actions;

import com.qvision.certificacion.models.IngresaLosDatosModel;
import com.qvision.certificacion.userinterfaces.PreciosHotelPages;
import net.thucydides.core.pages.PageObject;

import java.util.List;

import static com.qvision.certificacion.utils.Constantes.NUMERO_DIAS;
import static com.qvision.certificacion.utils.DiferenciaDeDias.diferenciaDeDias;
import static net.serenitybdd.core.Serenity.setSessionVariable;

public class BuscaElPrecio extends PageObject {
    private String mesInicio;
    private String diaInicio;
    private String annoInicio;
    private String mesFin;
    private String diaFin;
    private String annoFin;
    private PreciosHotelPages precios;

    public BuscaElPrecio(List<IngresaLosDatosModel> datos, PreciosHotelPages precios) {
        this.mesInicio = datos.get(0).getMesInicio();
        this.diaInicio = datos.get(0).getDiaInicio();
        this.annoInicio = datos.get(0).getAnoInicio();
        this.mesFin = datos.get(0).getMesFin();
        this.diaFin = datos.get(0).getDiaFin();
        this.annoFin = datos.get(0).getAnoFin();
        this.precios = precios;
    }

    public void action() {
        waitABit(2000);
        precios.buscarPrecioMasBajo();
        setSessionVariable(NUMERO_DIAS).to(diferenciaDeDias(diaInicio, mesInicio, annoInicio, diaFin, mesFin, annoFin));
    }

    public static void conLosDatos(List<IngresaLosDatosModel> datos, PreciosHotelPages precios) {
        new BuscaElPrecio(datos, precios).action();
    }
}
