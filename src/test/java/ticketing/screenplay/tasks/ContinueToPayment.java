package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.CheckoutPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Tarea Screenplay: continuar al paso de pago desde el checkout.
 *
 * Hace clic en [data-testid="payment-continue-btn"].
 * Debe ejecutarse después de {@link EnterEmail}.
 */
public class ContinueToPayment {

    /**
     * Devuelve la tarea que hace clic en "Continuar al pago".
     *
     * @return Performable ejecutable por un actor
     */
    public static Performable now() {
        return Task.where("{0} continúa hacia el pago",
                WaitUntil.the(CheckoutPage.CONTINUE_BTN, isClickable())
                         .forNoMoreThan(10).seconds(),
                Click.on(CheckoutPage.CONTINUE_BTN)
        );
    }
}
