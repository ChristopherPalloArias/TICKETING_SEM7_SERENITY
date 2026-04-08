package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la página de detalle / selección de tier del evento.
 *
 * Auditoría del frontend (2026-04-08) – TicketTier.tsx:
 *   • Cada tier card tiene  data-testid="tier-{tierType}"
 *     donde tierType es "GENERAL" | "VIP" | "EARLY_BIRD".
 *   • Un tier no disponible recibe  aria-disabled="true".
 *   • El span de nombre usa  className={styles.tierName}  (CSS Module).
 *   • El botón Reservar carece de data-testid; se localiza por texto.
 *
 * Parámetros Gherkin aceptados (coinciden con tierType del backend):
 *   "GENERAL" | "VIP" | "EARLY_BIRD"
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
     * Tier deshabilitado: localiza el div[@role='button'] con aria-disabled="true"
     * que contiene el span del nombre visible del tier.
     *
     * En TicketTier.tsx cuando disabled=true (!tier.isAvailable):
     *   &lt;motion.div
     *       data-testid={`tier-${tier.tierType}`}
     *       role="button"
     *       aria-disabled={disabled}    // "true" cuando !isAvailable
     *       tabIndex={-1}               // tabIndex=-1 cuando deshabilitado
     *   &gt;
     *     &lt;span className={styles.tierName}&gt;General | VIP | Early Bird&lt;/span&gt;
     *
     * Estrategia: aria-disabled="true" + span con nombre visible (más robusto que
     * data-testid, que puede eliminarse en algunos builds).
     *
     * Usado por: TierAvailabilityScreen.isTierUnavailable()
     *
     * @param tierName "GENERAL" | "VIP" | "EARLY_BIRD"  (equals tier.tierType in backend)
     * @return Target XPath apuntando al tier deshabilitado
     */
    public static Target tierDeshabilitado(String tierName) {
        String displayName = toDisplayName(tierName);
        return Target.the("tier " + displayName + " deshabilitado")
                     .locatedBy("//div[@role='button' and @aria-disabled='true']"
                              + "//span[normalize-space()='" + displayName + "']"
                              + "/ancestor::div[@role='button' and @aria-disabled='true'][1]");
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
