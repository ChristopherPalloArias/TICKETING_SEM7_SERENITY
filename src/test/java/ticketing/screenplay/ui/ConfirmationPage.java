package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Confirmación / Éxito de compra.
 *
 * Selector validado: "success-title" – título de la pantalla de éxito
 * que aparece tras completar el pago.
 */
public class ConfirmationPage {

    /** Título de la pantalla de confirmación exitosa. */
    public static final Target SUCCESS_TITLE =
            Target.the("título de pantalla de confirmación")
                  .locatedBy("[data-testid='success-title']");
}
