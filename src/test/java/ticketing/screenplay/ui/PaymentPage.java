package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Pago (Payment).
 *
 * Selector validado: "mock-payment-success" – botón que simula un pago exitoso
 * en el entorno E2E del frontend (activado con npm run dev:e2e).
 */
public class PaymentPage {

    /** Botón de simulación de pago exitoso (disponible sólo en modo E2E). */
    public static final Target MOCK_PAYMENT_SUCCESS_BTN =
            Target.the("botón simular pago exitoso")
                  .locatedBy("[data-testid='mock-payment-success']");
}
