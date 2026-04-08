package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Confirmación / Éxito de compra.
 *
 * NOTA – Auditoría del frontend (2026-04-08):
 * El título de éxito NO tiene data-testid.
 * HTML real: &lt;h1 class="_title_1xbpo_36"&gt;¡Pago aprobado!&lt;/h1&gt;
 * Se localiza por XPath sobre el texto del h1.
 */
public class ConfirmationPage {

    /**
     * Título de la pantalla de confirmación exitosa.
     * HTML real: ¡Pago aprobado!
     */
    public static final Target SUCCESS_TITLE =
            Target.the("título de pantalla de confirmación")
                  .locatedBy("//h1[normalize-space()='¡Pago aprobado!']");
}
