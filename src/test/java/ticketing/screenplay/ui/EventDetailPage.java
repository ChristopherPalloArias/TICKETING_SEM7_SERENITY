package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la página de detalle / selección de tier del evento.
 *
 * Tiers soportados en el MVP: VIP, GENERAL, EARLY_BIRD.
 * Selectores validados: data-testid="tier-{NOMBRE_TIER}" y "reserve-tier-btn".
 */
public class EventDetailPage {

    /** Botón de selección del tier VIP. */
    public static final Target TIER_VIP =
            Target.the("tier VIP")
                  .locatedBy("[data-testid='tier-VIP']");

    /** Botón de selección del tier GENERAL. */
    public static final Target TIER_GENERAL =
            Target.the("tier GENERAL")
                  .locatedBy("[data-testid='tier-GENERAL']");

    /** Botón de selección del tier EARLY_BIRD. */
    public static final Target TIER_EARLY_BIRD =
            Target.the("tier EARLY_BIRD")
                  .locatedBy("[data-testid='tier-EARLY_BIRD']");

    /** Botón para confirmar la reserva del tier seleccionado. */
    public static final Target RESERVE_BTN =
            Target.the("botón reservar tier")
                  .locatedBy("[data-testid='reserve-tier-btn']");

    /**
     * Devuelve el Target del tier según el nombre recibido como parámetro Gherkin.
     *
     * @param tierName  "VIP" | "GENERAL" | "EARLY_BIRD"
     * @return Target correspondiente al tier
     */
    public static Target tierButton(String tierName) {
        return Target.the("tier " + tierName)
                     .locatedBy("[data-testid='tier-" + tierName + "']");
    }
}
