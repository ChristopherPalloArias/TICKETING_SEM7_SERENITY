package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.EventsPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Tarea Screenplay: seleccionar el primer evento disponible en la cartelera.
 *
 * Hace clic en la primera tarjeta de evento que aparece en /eventos.
 * Usa espera explícita para garantizar que la tarjeta esté visible.
 */
public class SelectEvent {

    /**
     * Devuelve la tarea que selecciona el primer evento de la cartelera.
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
}
