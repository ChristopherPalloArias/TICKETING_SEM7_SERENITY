package ticketing.screenplay.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.screenplay.ui.EventDetailPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Tarea Screenplay: elegir un tier de precio dentro del detalle del evento.
 *
 * Acepta el nombre del tier como parámetro ("VIP", "GENERAL", "EARLY_BIRD").
 * El selector se resuelve dinámicamente via {@link EventDetailPage#tierButton(String)}.
 */
public class SelectTier {

    private final String tierName;

    private SelectTier(String tierName) {
        this.tierName = tierName;
    }

    /**
     * Fábrica estática. Uso: {@code SelectTier.called("GENERAL")}.
     *
     * @param tierName nombre del tier: "VIP" | "GENERAL" | "EARLY_BIRD"
     * @return instancia de SelectTier lista para usar como Performable
     */
    public static Performable called(String tierName) {
        return Task.where("{0} elige el tier '" + tierName + "'",
                WaitUntil.the(EventDetailPage.tierButton(tierName), isClickable())
                         .forNoMoreThan(10).seconds(),
                Click.on(EventDetailPage.tierButton(tierName))
        );
    }
}
