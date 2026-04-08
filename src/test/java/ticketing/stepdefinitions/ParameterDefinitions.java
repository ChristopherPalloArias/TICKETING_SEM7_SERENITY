package ticketing.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

/**
 * Definición del tipo de parámetro Cucumber {@code {actor}}.
 *
 * Permite escribir en Gherkin:
 *   Given que Marta es un invitado que desea comprar una entrada
 *   When Marta selecciona el primer evento disponible
 *
 * y que Serenity resuelva automáticamente la instancia de Actor.
 *
 * El escenario @Before inicializa el escenario con un Cast online
 * que gestiona actores con WebDriver.
 */
public class ParameterDefinitions {

    /**
     * Resuelve nombres en Gherkin como instancias de {@link Actor}.
     * Cualquier nombre (Marta, Juan, etc.) es resuelto por OnStage.
     *
     * @param actorName nombre del actor tal como aparece en el feature
     * @return Actor gestionado por Serenity Screenplay
     */
    @ParameterType(".*")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);
    }

    /**
     * Inicializa el escenario Screenplay antes de cada escenario.
     * OnlineCast provee actores con soporte de WebDriver.
     */
    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }
}
