package ticketing.screenplay.questions;

import net.serenitybdd.screenplay.Question;
import ticketing.screenplay.ui.ConfirmationPage;

/**
 * Pregunta Screenplay: inspecciona la pantalla de confirmación de compra.
 *
 * Uso en step definitions con {@code Ensure}:
 * <pre>
 *   actor.attemptsTo(
 *       Ensure.that(SuccessScreen.isVisible()).isTrue()
 *   );
 * </pre>
 *
 * O directamente usando el Target:
 * <pre>
 *   actor.attemptsTo(
 *       Ensure.that(ConfirmationPage.SUCCESS_TITLE).isDisplayed()
 *   );
 * </pre>
 */
public class SuccessScreen {

    /**
     * Pregunta si el título de confirmación [data-testid="success-title"] está visible.
     *
     * Implementada como lambda Screenplay para máxima compatibilidad con Serenity 5.x.
     *
     * @return Question&lt;Boolean&gt; – true si el elemento es visible
     */
    public static Question<Boolean> isVisible() {
        return actor -> ConfirmationPage.SUCCESS_TITLE
                .resolveFor(actor)
                .isDisplayed();
    }

    /**
     * Pregunta sobre el texto del título de confirmación.
     *
     * @return Question&lt;String&gt; – texto visible del elemento success-title
     */
    public static Question<String> title() {
        return actor -> ConfirmationPage.SUCCESS_TITLE
                .resolveFor(actor)
                .getText();
    }
}
