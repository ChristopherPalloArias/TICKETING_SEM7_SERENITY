package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la página de Cartelera de Eventos (/eventos).
 *
 * Selectores validados en la auditoría del frontend Ticketing MVP.
 * El selector [data-testid^="event-card-"] captura cualquier tarjeta
 * de evento (el sufijo es el ID del evento).
 */
public class EventsPage {

    /** Primera tarjeta de evento visible en la cartelera. */
    public static final Target PRIMER_EVENTO =
            Target.the("primera tarjeta de evento en cartelera")
                  .locatedBy("[data-testid^='event-card-']");

    /**
     * Tarjeta del evento identificada por su título exacto.
     * Usa XPath con aria-label "Ver {titulo}".
     *
     * Se usa XPath + concat() para manejar apóstrofes en el título
     * (ej. "The Phantom's Echo"), ya que el CSS selector no puede
     * contener comillas simples dentro de un valor entre comillas simples.
     *
     * @param titulo título visual del evento, ej. "The Phantom's Echo"
     * @return Target XPath apuntando a esa tarjeta
     */
    public static Target eventoPorTitulo(String titulo) {
        String xpathExpr = buildXPathAriaLabel("Ver " + titulo);
        return Target.the("tarjeta del evento '" + titulo + "'")
                     .locatedBy("//*[@aria-label=" + xpathExpr + "]");
    }

    /**
     * Construye una expresión XPath segura para un valor de atributo que puede
     * contener apóstrofes, usando la técnica concat().
     *
     * Ej.: "Ver The Phantom's Echo"
     *  →  concat('Ver The Phantom', "'", 's Echo')
     *
     * @param value cadena que puede contener apóstrofes
     * @return expresión XPath lista para embeber dentro de [@atributo=...]
     */
    private static String buildXPathAriaLabel(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        StringBuilder expr = new StringBuilder("concat(");
        String[] parts = value.split("'", -1);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                expr.append(", \"'\", ");
            }
            expr.append("'").append(parts[i]).append("'");
        }
        expr.append(")");
        return expr.toString();
    }

    /**
     * Botón hero de compra en la página de detalle del evento.
     * data-testid real confirmado en auditoría del frontend: "hero-reservar-btn".
     */
    public static final Target HERO_BUY_BTN =
            Target.the("botón hero de compra")
                  .locatedBy("[data-testid='hero-reservar-btn']");
}
