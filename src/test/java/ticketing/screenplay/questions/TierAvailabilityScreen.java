package ticketing.screenplay.questions;

import net.serenitybdd.screenplay.Question;
import ticketing.screenplay.ui.EventDetailPage;

/**
 * Pregunta Screenplay: estado de disponibilidad de un tier en la pantalla
 * de detalle del evento (HU-03).
 *
 * ── Precondición de datos ────────────────────────────────────────────────
 * Los escenarios que usan esta clase requieren seed data controlado:
 *   - isTierUnavailable         → evento con stock = 0 en el tier indicado
 *   - isTierUnavailableOrAbsent → evento con early_bird_expiry < hoy
 *
 * ── Supuesto de DOM ───────────────────────────────────────────────────────
 * Se asume que el frontend señala el estado "no disponible" mediante
 * aria-disabled="true" en el div[@role='button'] del tier card.
 * Esta es la convención estándar de React + WAI-ARIA para elementos
 * interactivos deshabilitados.
 *
 * VERIFICAR con devtools antes de activar los escenarios @requires-seed-data:
 *   1. Reproducir el estado (stock=0 / early_bird_expiry vencido) en el entorno.
 *   2. Inspeccionar el elemento tier card con F12.
 *   3. Confirmar presencia de aria-disabled="true" en el div[@role='button'].
 *   4. Si el atributo es distinto, actualizar EventDetailPage.tierDeshabilitado().
 *
 * ── Caso Early Bird ───────────────────────────────────────────────────────
 * El frontend puede optar por ocultar el tier del DOM en lugar de mostrarlo
 * deshabilitado. isTierUnavailableOrAbsent() cubre ambas variantes:
 *   - tier presente + aria-disabled="true"  → true
 *   - tier completamente ausente del DOM    → true
 *   - tier presente + interaccionable       → false (no debería ocurrir)
 */
public class TierAvailabilityScreen {

    private TierAvailabilityScreen() {
        // Clase utilitaria — sin instancias
    }

    /**
     * Pregunta si un tier está visualmente marcado como no disponible.
     *
     * Retorna true si el tier existe en el DOM y está marcado con
     * aria-disabled="true" (supuesto DOM — verificar antes de activar).
     *
     * Retorna false si el tier está habilitado o si no existe en el DOM.
     *
     * @param tierName nombre técnico del tier ("GENERAL", "VIP", "EARLY_BIRD")
     * @return Question&lt;Boolean&gt;
     */
    public static Question<Boolean> isTierUnavailable(String tierName) {
        return actor -> {
            try {
                return EventDetailPage.tierDeshabilitado(tierName)
                        .resolveFor(actor)
                        .isDisplayed();
            } catch (Exception e) {
                // Elemento no encontrado → tier ausente del DOM → no está deshabilitado visualmente
                return false;
            }
        };
    }

    /**
     * Pregunta si un tier está no disponible, ya sea deshabilitado o ausente del DOM.
     *
     * Cubre ambas variantes de comportamiento del frontend:
     *   - El tier se muestra con aria-disabled="true" (no seleccionable)
     *   - El tier se oculta completamente del DOM (no aparece)
     *
     * Retorna true en cualquiera de los dos casos.
     * Retorna false solo si el tier existe y está habilitado para la compra.
     *
     * @param tierName nombre técnico del tier ("GENERAL", "VIP", "EARLY_BIRD")
     * @return Question&lt;Boolean&gt;
     */
    public static Question<Boolean> isTierUnavailableOrAbsent(String tierName) {
        return actor -> {
            try {
                // Verificar primero si el tier existe en el DOM de forma habilitada
                boolean habilitado = EventDetailPage.tierButton(tierName)
                        .resolveAllFor(actor)
                        .stream()
                        .anyMatch(el -> {
                            try {
                                // Si resolveAllFor devuelve elementos, comprobar que no estén disabled
                                String ariaDisabled = el.getAttribute("aria-disabled");
                                return ariaDisabled == null || !ariaDisabled.equals("true");
                            } catch (Exception ex) {
                                return false;
                            }
                        });
                if (!habilitado) {
                    return true; // ningún elemento habilitado encontrado → no disponible
                }
                // Si hay un elemento habilitado, verificar el selector de deshabilitado
                return EventDetailPage.tierDeshabilitado(tierName)
                        .resolveFor(actor)
                        .isDisplayed();
            } catch (Exception e) {
                // tier completamente ausente del DOM → no disponible para selección
                return true;
            }
        };
    }
}
