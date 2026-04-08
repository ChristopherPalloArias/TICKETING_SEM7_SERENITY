package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.PaymentPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Tarea Screenplay: interactuar con los botones de pago simulado.
 *
 * Provee dos variantes:
 *   - {@link #successfully()} → simula pago aprobado
 *   - {@link #withFailure()}  → simula pago rechazado
 *
 * Ambos botones son visibles solo en la pantalla de pago mock del frontend.
 * No representan pagos reales. El selector es XPath por texto visible.
 */
public class CompletePayment {

    /**
     * Simula un pago exitoso haciendo clic en "Simular Pago Exitoso".
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

    /**
     * Simula un pago rechazado haciendo clic en "Simular Pago Rechazado".
     * Tras el clic el frontend navega a la pantalla de pago declinado.
     *
     * @return Performable ejecutable por un actor
     */
    public static Performable withFailure() {
        return Task.where("{0} simula un pago rechazado",
                WaitUntil.the(PaymentPage.MOCK_PAYMENT_FAILURE_BTN, isClickable())
                         .forNoMoreThan(10).seconds(),
                Click.on(PaymentPage.MOCK_PAYMENT_FAILURE_BTN)
        );
    }
}
