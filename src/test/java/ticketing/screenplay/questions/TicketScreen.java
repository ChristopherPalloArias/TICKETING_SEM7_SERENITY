package ticketing.screenplay.questions;

import net.serenitybdd.screenplay.Question;
import ticketing.screenplay.ui.ConfirmationPage;

/**
 * Pregunta Screenplay: inspecciona la presencia o ausencia del ticket
 * de compra confirmado en pantalla.
 *
 * Construida sobre {@link ConfirmationPage#SUCCESS_TITLE} para no duplicar
 * lógica de localización. Complementa a {@link SuccessScreen} con semántica
 * orientada a la HU de visibilidad de ticket.
 *
 * Uso en step definitions con {@code Ensure}:
 * <pre>
 *   actor.attemptsTo(
 *       Ensure.that(TicketScreen.isConfirmed()).isTrue()
 *   );
 *   actor.attemptsTo(
 *       Ensure.that(TicketScreen.isAbsent()).isTrue()
 *   );
 * </pre>
 */
public class TicketScreen {

    private TicketScreen() {
        // Clase utilitaria — sin instancias
    }

    /**
     * Pregunta si el ticket de compra confirmado (¡Pago aprobado!) es visible.
     *
     * Delega en {@link ConfirmationPage#SUCCESS_TITLE}.
     * Retorna {@code true} si el elemento está presente y visible en el DOM.
     *
     * @return Question&lt;Boolean&gt;
     */
    public static Question<Boolean> isConfirmed() {
        return actor -> ConfirmationPage.SUCCESS_TITLE
                .resolveFor(actor)
                .isDisplayed();
    }

    /**
     * Pregunta el texto del título de la pantalla de confirmación de ticket.
     *
     * Permite hacer assertions sobre el contenido exacto del mensaje de éxito,
     * cubriendo el requisito "información principal del ticket está en pantalla".
     *
     * @return Question&lt;String&gt; – texto visible del elemento de confirmación
     */
    public static Question<String> confirmationTitle() {
        return actor -> ConfirmationPage.SUCCESS_TITLE
                .resolveFor(actor)
                .getText();
    }

    /**
     * Pregunta si el ticket de compra confirmado está AUSENTE de pantalla.
     *
     * Implementación defensiva con try-catch: si el elemento no existe en el DOM
     * (caso normal después de un pago rechazado), se retorna {@code true}.
     * Si existe pero no es visible, también retorna {@code true}.
     * Solo retorna {@code false} si el elemento existe Y está visible.
     *
     * No usa Thread.sleep(). Actúa sobre el estado actual del DOM en el momento
     * en que ya se validó la pantalla de fallo como precondición en el escenario.
     *
     * @return Question&lt;Boolean&gt; – true cuando no hay ticket confirmado en pantalla
     */
    public static Question<Boolean> isAbsent() {
        return actor -> {
            try {
                return !ConfirmationPage.SUCCESS_TITLE
                        .resolveFor(actor)
                        .isDisplayed();
            } catch (Exception e) {
                // Elemento no encontrado en el DOM → ticket ausente → correcto
                return true;
            }
        };
    }
}
