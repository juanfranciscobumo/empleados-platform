package com.co.qvision.certificacion.steps;

import com.qvision.certificacion.actions.BuscaElPrecio;
import com.qvision.certificacion.actions.IngresaLosDatos;
import com.qvision.certificacion.builders.IngresarLosDatosBuilder;
import com.qvision.certificacion.models.IngresaLosDatosModel;
import com.qvision.certificacion.userinterfaces.AgenciaDeViajesPages;
import com.qvision.certificacion.userinterfaces.PreciosHotelPages;
import com.qvision.certificacion.userinterfaces.TarifaNetaPages;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static com.qvision.certificacion.utils.CalcularTarifa.laTarifa;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AgenciaDeViajesStepDefinitions {

    AgenciaDeViajesPages agenciaDeViajes;
    PreciosHotelPages preciosHotelPages;
    TarifaNetaPages tarifaNetaPages;

    @Given("que el usuario se encuentre en la pagina web")
    public void queElUsuarioSeEncuentreEnLaPaginaWeb() {
        agenciaDeViajes.open();
    }

    @When("realice la busqueda con los datos solicitados")
    public void realiceLaBusquedaConLosDatosSolicitados(List<IngresaLosDatosModel> datos) {
        IngresaLosDatos.aLaPagina(IngresarLosDatosBuilder
                .location(datos.get(0).getLocation())
                .adults(datos.get(0).getAdults())
                .diaInicio(datos.get(0).getDiaInicio())
                .diaFin(datos.get(0).getDiaFin())
                .anoFin(datos.get(0).getAnoFin())
                .anoInicio(datos.get(0).getAnoInicio())
                .mesFin(datos.get(0).getMesFin())
                .mesInicio(datos.get(0).getMesInicio())
                .rooms(datos.get(0).getRooms())
                .children(datos.get(0).getChildren()), agenciaDeViajes);
    }

    @When("buscara el precio mas economico")
    public void buscaraElPrecioMasEconomico(List<IngresaLosDatosModel> datos) {
        BuscaElPrecio.conLosDatos(datos, preciosHotelPages);
    }

    @Then("valide el precio Total")
    public void valideElPrecioTotal() {
        assertEquals(laTarifa(), tarifaNetaPages.tarifaNeta());
    }

}
