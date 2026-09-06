package com.qvision.certificacion.actions;

import com.qvision.certificacion.interactions.BuscaLaFecha;
import com.qvision.certificacion.models.IngresaLosDatosModel;
import com.qvision.certificacion.userinterfaces.AgenciaDeViajesPages;
import net.thucydides.core.pages.PageObject;

import static com.qvision.certificacion.userinterfaces.AgenciaDeViajesPages.*;

public class IngresaLosDatos extends PageObject {
    private IngresaLosDatosModel datos;
    private AgenciaDeViajesPages agenciaDeViajes;

    public IngresaLosDatos(IngresaLosDatosModel datos, AgenciaDeViajesPages agenciaDeViajes) {
        this.datos = datos;
        this.agenciaDeViajes = agenciaDeViajes;
    }

    public void action() {
        selUbicacion.click();
        agenciaDeViajes.abrirUbicaciones(datos.getLocation());
        calendario.click();
        BuscaLaFecha.paraSeleccionarla(btnmesinicial, btnNext, ltDias, datos.getMesInicio(),
                datos.getAnoInicio(), datos.getDiaInicio());
        calendarioDos.click();
        BuscaLaFecha.paraSeleccionarla(btnmesinicialdos, btnNextDos, ltDiasDos, datos.getMesFin(),
                datos.getAnoFin(), datos.getDiaFin());
        ltRoom.click();
        waitABit(3000);
        agenciaDeViajes.seleccionarRooms(datos.getRooms());
        ltAdults.click();
        waitABit(3000);
        agenciaDeViajes.seleccionarAdultos(datos.getAdults());
        ltChildren.click();
        waitABit(3000);
        agenciaDeViajes.seleccionarChildren(datos.getChildren());
        btnSearch.click();

    }

    public static void aLaPagina(IngresaLosDatosModel datos, AgenciaDeViajesPages agenciaDeViajes) {
        new IngresaLosDatos(datos, agenciaDeViajes).action();
    }
}
