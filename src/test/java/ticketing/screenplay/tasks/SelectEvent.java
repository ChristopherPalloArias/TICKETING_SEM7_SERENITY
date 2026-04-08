package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.EventsPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Tarea Screenplay: seleccionar un evento de la cartelera.
 *
 * Soporta selección por primer elemento visible y por título exacto.
 * Usa espera explícita para garantizar que la tarjeta esté visible antes del clic.
 */
public class SelectEvent {

    /**
     * Selecciona el primer evento visible de la cartelera.
     *
     * @return Performable ejecutable por un actor
     */
    public static Performable elPrimeroDisponible() {
        return Task.where("{0} selecciona el primer evento disponible en cartelera",
                WaitUntil.the(EventsPage.PRIMER_EVENTO, isVisible())
                         .forNoMoreThan(10).seconds(),
                Click.on(EventsPage.PRIMER_EVENTO)
        );
    }

    /**
     * Selecciona el evento cuyo título visible coincide exactamente con {@code titulo}.
     * Usa el atributo aria-label="Ver {titulo}" que el frontend asigna a cada tarjeta.
     *
     * @param titulo título exacto del evento, ej. "The Phantom's Echo"
     * @return Performable ejecutable por un actor
     */
    public static Performable porTitulo(String titulo) {
        return Task.where("{0} selecciona el evento '" + titulo + "'",
                WaitUntil.the(EventsPage.eventoPorTitulo(titulo), isVisible())
                         .forNoMoreThan(10).seconds(),
                Click.on(EventsPage.eventoPorTitulo(titulo))
        );
    }
}
