package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la página de detalle / selección de tier del evento.
 *
 * NOTA IMPORTANTE – Auditoría del frontend (2026-04-08):
 * Los tier cards NO tienen data-testid. Se identifican por el texto visible
 * dentro del span de nombre, usando XPath. Los nombres reales en el DOM son:
 *   "General" | "VIP" | "Early Bird"
 *
 * El botón Reservar tampoco tiene data-testid; se localiza por su texto.
 *
 * Parámetros Gherkin aceptados → nombre real en DOM:
 *   "GENERAL"    → "General"
 *   "VIP"        → "VIP"
 *   "EARLY_BIRD" → "Early Bird"
 */
public class EventDetailPage {

    /**
     * Tier General: div[@role='button'] que contiene el span con texto "General".
     * XPath: sube al ancestro div con role=button desde el span de nombre.
     */
    public static final Target TIER_GENERAL =
            Target.the("tier General")
                  .locatedBy("//div[@role='button']//span[normalize-space()='General']/ancestor::div[@role='button'][1]");

    /** Tier VIP: mismo patrón con texto "VIP". */
    public static final Target TIER_VIP =
            Target.the("tier VIP")
                  .locatedBy("//div[@role='button']//span[normalize-space()='VIP']/ancestor::div[@role='button'][1]");

    /** Tier Early Bird: mismo patrón con texto "Early Bird". */
    public static final Target TIER_EARLY_BIRD =
            Target.the("tier Early Bird")
                  .locatedBy("//div[@role='button']//span[normalize-space()='Early Bird']/ancestor::div[@role='button'][1]");

    /**
     * Botón Reservar: identificado por el texto del span interno.
     * HTML real: &lt;button&gt;&lt;span&gt;Reservar&lt;/span&gt;&lt;/button&gt;
     */
    public static final Target RESERVE_BTN =
            Target.the("botón Reservar")
                  .locatedBy("//button[.//span[normalize-space()='Reservar']]");

    /**
     * Tier deshabilitado: mismo patrón XPath pero restringido a
     * div[@role='button' and @aria-disabled='true'].
     *
     * ── SUPUESTO DE DOM ────────────────────────────────────────────────────
     * Se asume que el frontend marca un tier no disponible con aria-disabled="true"
     * en el div[@role='button']. Esta es la convención WAI-ARIA estándar.
     *
     * VERIFICAR con devtools antes de ejecutar escenarios @requires-seed-data:
     *   - Con un evento de stock=0 activo, inspeccionar el tier card.
     *   - Si el atributo real es diferente (p.ej. clase CSS, data-attr),
     *     actualizar el XPath de este método.
     *
     * Usado por: TierAvailabilityScreen.isTierUnavailable()
     *
     * @param tierName "GENERAL" | "VIP" | "EARLY_BIRD"
     * @return Target XPath apuntando al tier deshabilitado
     */
    public static Target tierDeshabilitado(String tierName) {
        String displayName = toDisplayName(tierName);
        return Target.the("tier " + displayName + " deshabilitado")
                     .locatedBy("//div[@role='button' and @aria-disabled='true']"
                              + "//span[normalize-space()='" + displayName + "']"
                              + "/ancestor::div[@role='button'][1]");
    }

    /**
     * Devuelve el Target del tier según el nombre recibido como parámetro Gherkin.
     * Convierte el nombre técnico del feature al texto visible real en el DOM.
     *
     * @param tierName  "GENERAL" | "VIP" | "EARLY_BIRD"
     * @return Target XPath correspondiente al tier
     */
    public static Target tierButton(String tierName) {
        String displayName = toDisplayName(tierName);
        return Target.the("tier " + displayName)
                     .locatedBy("//div[@role='button']//span[normalize-space()='" + displayName + "']/ancestor::div[@role='button'][1]");
    }

    /**
     * Convierte el identificador técnico del feature al nombre visible en el frontend.
     *
     * @param tierName nombre técnico del feature (ej. "EARLY_BIRD")
     * @return nombre real visible en el DOM (ej. "Early Bird")
     */
    private static String toDisplayName(String tierName) {
        switch (tierName.toUpperCase()) {
            case "GENERAL":    return "General";
            case "VIP":        return "VIP";
            case "EARLY_BIRD": return "Early Bird";
            default:           return tierName;
        }
    }
}
