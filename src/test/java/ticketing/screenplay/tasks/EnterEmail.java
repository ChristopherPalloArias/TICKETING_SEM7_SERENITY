package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.CheckoutPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Tarea Screenplay: ingresar el correo electrónico del invitado en el campo de checkout.
 *
 * Limpia el campo antes de escribir para evitar concatenaciones accidentales.
 * Selector validado: [data-testid="checkout-email-input"].
 */
public class EnterEmail {

    /**
     * Devuelve la tarea que ingresa el email en el campo de checkout.
     *
     * @param email dirección de correo a ingresar
     * @return Performable ejecutable por un actor
     */
    public static Performable withAddress(String email) {
        return Task.where("{0} ingresa el correo '" + email + "' en el checkout",
                WaitUntil.the(CheckoutPage.EMAIL_INPUT, isVisible())
                         .forNoMoreThan(10).seconds(),
                Clear.field(CheckoutPage.EMAIL_INPUT),
                Enter.theValue(email).into(CheckoutPage.EMAIL_INPUT)
        );
    }
}
