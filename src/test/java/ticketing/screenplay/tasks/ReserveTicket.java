package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.EventDetailPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Tarea Screenplay: confirmar la reserva del tier seleccionado.
 *
 * Hace clic en el botón [data-testid="reserve-tier-btn"].
 * Debe ejecutarse después de {@link SelectTier}.
 */
public class ReserveTicket {

    /**
     * Devuelve la tarea que confirma la reserva.
     *
     * @return Performable ejecutable por un actor
     */
    public static Performable now() {
        return Task.where("{0} reserva su lugar haciendo clic en 'Reservar'",
                WaitUntil.the(EventDetailPage.RESERVE_BTN, isClickable())
                         .forNoMoreThan(10).seconds(),
                Click.on(EventDetailPage.RESERVE_BTN)
        );
    }
}
