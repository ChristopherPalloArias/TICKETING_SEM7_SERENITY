package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.PaymentPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Tarea Screenplay: simular pago exitoso mediante el mock de E2E.
 *
 * Hace clic en [data-testid="mock-payment-success"].
 * Este botón sólo está disponible cuando el frontend se ejecuta con:
 *   npm run dev:e2e
 *
 * NO representa un pago real. Es exclusivo para automatización en modo E2E.
 */
public class CompletePayment {

    /**
     * Devuelve la tarea que simula el pago exitoso.
     *
     * @return Performable ejecutable por un actor
     */
    public static Performable successfully() {
        return Task.where("{0} completa el pago simulado exitosamente",
                WaitUntil.the(PaymentPage.MOCK_PAYMENT_SUCCESS_BTN, isClickable())
                         .forNoMoreThan(10).seconds(),
                Click.on(PaymentPage.MOCK_PAYMENT_SUCCESS_BTN)
        );
    }
}
