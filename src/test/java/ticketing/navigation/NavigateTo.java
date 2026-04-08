package ticketing.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

/**
 * Tarea de navegación al home/cartelera de eventos.
 *
 * La URL base se toma de serenity.conf (entorno activo) y puede sobreescribirse
 * en tiempo de ejecución con:
 *   -Dwebdriver.base.url=http://localhost:3000
 *   -Denvironment=staging
 */
public class NavigateTo {

    /**
     * Navega a la ruta /eventos de la aplicación Ticketing MVP.
     *
     * @return tarea Screenplay ejecutable por un actor
     */
    public static Performable laCartelera() {
        return Task.where("{0} navega a la cartelera de eventos",
                Open.url("/eventos")
        );
    }

    /**
     * Navega a la URL raíz de la aplicación.
     *
     * @return tarea Screenplay ejecutable por un actor
     */
    public static Performable elHomeDelSitio() {
        return Task.where("{0} navega al home del sitio",
                Open.url("/")
        );
    }
}
