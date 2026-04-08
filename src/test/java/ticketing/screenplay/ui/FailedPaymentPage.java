package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Pago Rechazado.
 *
 * Auditoría del DOM real (2026-04-08):
 * Ningún elemento en esta pantalla tiene data-testid.
 * Se usa XPath por texto visible, mismo patrón que el resto del proyecto.
 *
 * HTML de referencia:
 *   <strong class="_title_13o10_33">Pago declinado.</strong>
 *   <button class="_retryBtn_al8w4_67"><span>Reintentar Pago (Intento N de 3)</span></button>
 *   <button class="_changeBtn_al8w4_93">Usar otro método de pago</button>
 */
public class FailedPaymentPage {

    /**
     * Título de la pantalla de rechazo.
     * Texto real: "Pago declinado."
     */
    public static final Target FAILURE_TITLE =
            Target.the("título de pago declinado")
                  .locatedBy("//strong[normalize-space()='Pago declinado.']");

    /**
     * Botón de reintento de pago.
     * El texto incluye el número de intento (ej. "Reintentar Pago (Intento 2 de 3)"),
     * por eso se usa contains() para soportar cualquier número de intento.
     */
    public static final Target RETRY_BTN =
            Target.the("botón reintentar pago")
                  .locatedBy("//button[contains(normalize-space(.), 'Reintentar Pago')]");

    /**
     * Botón alternativo para cambiar método de pago.
     * Se incluye como referencia para futuros escenarios.
     */
    public static final Target CHANGE_PAYMENT_BTN =
            Target.the("botón cambiar método de pago")
                  .locatedBy("//button[normalize-space()='Usar otro método de pago']");
}
