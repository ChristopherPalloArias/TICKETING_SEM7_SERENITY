package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Pago simulado.
 *
 * NOTA – Auditoría del frontend (2026-04-08):
 * Los botones NO tienen data-testid. Se identifican por el texto
 * visible dentro del h3 anidado en cada botón.
 *
 * Botón de pago exitoso: "Simular Pago Exitoso"
 * Botón de pago rechazado: "Simular Pago Rechazado"
 */
public class PaymentPage {

    /**
     * Botón para simular un pago aprobado.
     * HTML real: &lt;button&gt;&lt;h3&gt;Simular Pago Exitoso&lt;/h3&gt;&lt;/button&gt;
     */
    public static final Target MOCK_PAYMENT_SUCCESS_BTN =
            Target.the("botón simular pago exitoso")
                  .locatedBy("//button[.//h3[normalize-space()='Simular Pago Exitoso']]");

    /**
     * Botón para simular un pago rechazado.
     * HTML real: &lt;button&gt;&lt;h3&gt;Simular Pago Rechazado&lt;/h3&gt;&lt;/button&gt;
     */
    public static final Target MOCK_PAYMENT_FAILURE_BTN =
            Target.the("botón simular pago rechazado")
                  .locatedBy("//button[.//h3[normalize-space()='Simular Pago Rechazado']]");
}
