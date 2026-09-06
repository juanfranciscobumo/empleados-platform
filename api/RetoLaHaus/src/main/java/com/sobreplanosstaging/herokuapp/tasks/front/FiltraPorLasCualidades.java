package com.sobreplanosstaging.herokuapp.tasks.front;

import com.sobreplanosstaging.herokuapp.interactions.*;
import com.sobreplanosstaging.herokuapp.models.front.FiltrosModel;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.*;

import static com.sobreplanosstaging.herokuapp.userinterfaces.FiltrosPage.*;

public class FiltraPorLasCualidades implements Task {
    private String bannos;
    private String areaMinima;
    private String areaMaxima;
    private String apartamento;

    public FiltraPorLasCualidades(FiltrosModel filtro) {
        this.bannos = filtro.getBannos();
        this.areaMaxima = filtro.getAreaMaxima();
        this.areaMinima = filtro.getAreaMinima();
        this.apartamento = filtro.getApartamento();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(BTN_INCREMENTAR_BANNOS),
                IngresarNumero.deUnidades(bannos, BTN_INCREMENTAR_BANNOS, TXT_NUMERO_BANNOS),
                MueveLaBarra.hacia(areaMinima, BARRA_AREA_MINIMA, INPUT_AREA_MINIMA, 1),
                MueveLaBarra.hacia(areaMaxima, BARRA_AREA_MAXIMA, INPUT_AREA_MAXIMA, -2),
                Click.on(APARTAMENTO.of(apartamento)),
                Switch.toNewWindow());
    }

    public static FiltraPorLasCualidades deLaPropiedad(FiltrosModel filtro) {
        return Tasks.instrumented(FiltraPorLasCualidades.class, filtro);
    }
}
