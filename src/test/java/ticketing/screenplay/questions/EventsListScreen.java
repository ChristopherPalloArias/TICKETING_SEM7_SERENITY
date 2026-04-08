package ticketing.screenplay.questions;

import net.serenitybdd.screenplay.Question;
import ticketing.screenplay.ui.EventsPage;

/**
 * Pregunta Screenplay: inspecciona el contenido de la cartelera de eventos.
 *
 * Construida sobre {@link EventsPage#PRIMER_EVENTO} (selector CSS
 * {@code [data-testid^='event-card-']}), que captura cualquier tarjeta
 * de evento publicada en la cartelera, independientemente de su ID.
 *
 * No duplica lógica de navegación ni de selección de evento — solo
 * inspecciona el estado visible de la cartelera.
 *
 * Uso en step definitions:
 * <pre>
 *   actor.attemptsTo(
 *       Ensure.that(EventsListScreen.hasAtLeastOneEvent()).isTrue()
 *   );
 *   actor.attemptsTo(
 *       Ensure.that(EventsListScreen.visibleEventCount()).isGreaterThan(0)
 *   );
 * </pre>
 */
public class EventsListScreen {

    private EventsListScreen() {
        // Clase utilitaria — sin instancias
    }

    /**
     * Pregunta si la cartelera muestra al menos un evento disponible.
     *
     * Usa {@code resolveAllFor} para obtener todos los elementos que coincidan
     * con el selector {@code [data-testid^='event-card-']} y verifica que
     * la lista no esté vacía.
     *
     * No realiza espera interna: la espera explícita se delega al step definition
     * mediante {@code WaitUntil}, siguiendo el patrón establecido en el proyecto.
     *
     * @return Question&lt;Boolean&gt; — true si hay al menos una tarjeta de evento visible
     */
    public static Question<Boolean> hasAtLeastOneEvent() {
        return actor -> !EventsPage.PRIMER_EVENTO
                .resolveAllFor(actor)
                .isEmpty();
    }

    /**
     * Pregunta cuántos eventos están visibles en la cartelera.
     *
     * Permite hacer assertions numéricas.
     * Ej.: {@code Ensure.that(EventsListScreen.visibleEventCount()).isGreaterThan(0)}
     *
     * @return Question&lt;Integer&gt; — número de tarjetas de evento renderizadas
     */
    public static Question<Integer> visibleEventCount() {
        return actor -> EventsPage.PRIMER_EVENTO
                .resolveAllFor(actor)
                .size();
    }
}
