package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Checkout (ingreso de datos del invitado).
 *
 * Selectores validados: "checkout-email-input", "payment-continue-btn".
 */
public class CheckoutPage {

    /** Campo de ingreso del correo electrónico del invitado. */
    public static final Target EMAIL_INPUT =
            Target.the("campo email del invitado")
                  .locatedBy("[data-testid='checkout-email-input']");

    /** Botón para continuar al paso de pago. */
    public static final Target CONTINUE_BTN =
            Target.the("botón continuar al pago")
                  .locatedBy("[data-testid='payment-continue-btn']");
}
