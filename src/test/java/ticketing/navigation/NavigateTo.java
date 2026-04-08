package ticketing.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

/**
 * Tarea de navegación al home/cartelera de eventos.
 *
 * IMPORTANTE: Open.url() de Serenity Screenplay NO resuelve webdriver.base.url
 * automáticamente — ChromeDriver exige una URL absoluta. Por eso se construye
 * aquí la URL completa leyendo la propiedad del sistema en tiempo de ejecución.
 *
 * La URL base se lee en este orden de prioridad:
 *   1. Propiedad JVM: -Dwebdriver.base.url=http://localhost:5173  (CLI o systemPropertyVariables de Surefire)
 *   2. Valor de reserva: http://localhost:3000
 *
 * Ejemplos de uso:
 *   mvn clean verify -Dwebdriver.base.url=http://localhost:5173
 *   mvn clean verify -Dwebdriver.base.url=https://staging.ticketing.example.com
 */
public class NavigateTo {

    /**
     * Lee la URL base desde la propiedad de sistema JVM.
     * Se resuelve en tiempo de ejecucion para respetar el valor pasado por CLI.
     */
    private static String baseUrl() {
        return System.getProperty("webdriver.base.url", "http://localhost:3000")
                     .replaceAll("/$", "");
    }

    /**
     * Navega a /eventos con URL absoluta completa.
     *
     * @return tarea Screenplay ejecutable por un actor
     */
    public static Performable laCartelera() {
        return Task.where("{0} navega a la cartelera de eventos",
                Open.url(baseUrl() + "/eventos")
        );
    }

    /**
     * Navega a la raiz del sitio con URL absoluta completa.
     *
     * @return tarea Screenplay ejecutable por un actor
     */
    public static Performable elHomeDelSitio() {
        return Task.where("{0} navega al home del sitio",
                Open.url(baseUrl() + "/")
        );
    }
}
