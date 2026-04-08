package ticketing.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Interacción personalizada exclusiva para propósitos de DEMOSTRACIÓN.
 * Inserta una pausa real (Thread.sleep) para ralentizar el flujo visualmente
 * de manera que los espectadores humanos puedan observar qué está ocurriendo en pantalla.
 */
public class TakeABreath implements Interaction {

    private final int millis;

    public TakeABreath(int millis) {
        this.millis = millis;
    }

    public static TakeABreath of(int millis) {
        return instrumented(TakeABreath.class, millis);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
