package com.sobreplanosstaging.herokuapp.stepdefinitions.front;

import com.sobreplanosstaging.herokuapp.builders.BusquedaBuilder;
import com.sobreplanosstaging.herokuapp.builders.FiltrosBuilder;
import com.sobreplanosstaging.herokuapp.exceptions.Exceptions;
import com.sobreplanosstaging.herokuapp.models.front.BusquedaModel;
import com.sobreplanosstaging.herokuapp.models.front.FiltrosModel;
import com.sobreplanosstaging.herokuapp.questions.ElInmuebleConsultado;
import com.sobreplanosstaging.herokuapp.tasks.front.FiltraPorLasCualidades;
import com.sobreplanosstaging.herokuapp.tasks.front.RealizaLaBusqueda;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.sobreplanosstaging.herokuapp.exceptions.Exceptions.*;
import static com.sobreplanosstaging.herokuapp.utils.constantes.Constantes.URL_SOBRE_PLANOS;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class ConsultaPropiedadStepDefinition {

    public WebDriver driver;

    @Dado("que el usuario se encuentre en sobreplanos-staging")
    public void queElUsuarioSeEncuentreEnSobreplanosStaging() {
        theActorInTheSpotlight().whoCan(BrowseTheWeb.with(driver))
                .attemptsTo(Open.url(URL_SOBRE_PLANOS));
    }

    @Cuando("ingrese los datos de la propiedad que desee consultar")
    public void ingreseLosDatosDeLaPropiedadQueDeseeConsultar(List<BusquedaModel> datos) {
        theActorInTheSpotlight().attemptsTo(RealizaLaBusqueda.deLaPropiedad(BusquedaBuilder
                .tipoDePropiedad(datos.get(0).getTipoPropiedad())
                .barrio(datos.get(0).getBarrio())
                .habitaciones(datos.get(0).getHabitaciones())));
    }

    @Cuando("filtre por las cualidades de la propiedad")
    public void filtrePorLasCualidadesDeLaPropiedad(List<FiltrosModel> datos) {
        theActorInTheSpotlight().attemptsTo(FiltraPorLasCualidades.deLaPropiedad(FiltrosBuilder
                .apartamento(datos.get(0).getApartamento())
                .areaMinima(datos.get(0).getAreaMinima())
                .areaMaxima(datos.get(0).getAreaMaxima())
                .bannos(datos.get(0).getBannos())));
    }

    @Entonces("el usuario vera los datos del inmueble buscado")
    public void elUsuarioVeraLosDatosDelInmuebleBuscado(List<FiltrosModel> datos) {
        theActorInTheSpotlight().should(
                seeThat(ElInmuebleConsultado.conHabitacionesEs(), equalTo(datos.get(0).getHabitaciones()))
                        .orComplainWith(Exceptions.class, HABITACIONES),
                seeThat(ElInmuebleConsultado.conBannosEs(), equalTo(datos.get(0).getBannos()))
                        .orComplainWith(Exceptions.class, BANNOS),
                seeThat(ElInmuebleConsultado.conNombre(), equalTo(datos.get(0).getApartamento()))
                        .orComplainWith(Exceptions.class, NOMBRE_PROYECTO));
    }
}
