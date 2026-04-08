package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la página de Cartelera de Eventos (/eventos).
 *
 * Selectores validados en la auditoría del frontend Ticketing MVP.
 * El selector [data-testid^="event-card-"] captura cualquier tarjeta
 * de evento (el sufijo es el ID del evento).
 */
public class EventsPage {

    /** Primera tarjeta de evento visible en la cartelera. */
    public static final Target PRIMER_EVENTO =
            Target.the("primera tarjeta de evento en cartelera")
                  .locatedBy("[data-testid^='event-card-']");

    /**
     * Botón hero de compra directa (si la página de inicio muestra un hero).
     * Alternativa a PRIMER_EVENTO cuando se navega desde el home.
     */
    public static final Target HERO_BUY_BTN =
            Target.the("botón hero de compra")
                  .locatedBy("[data-testid='hero-buy-btn']");
}
