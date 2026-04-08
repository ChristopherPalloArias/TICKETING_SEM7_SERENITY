package ticketing.screenplay.ui;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Locators de la pantalla de Checkout (ingreso de datos del invitado).
 *
 * NOTA – Auditoría del frontend (2026-04-08):
 * El campo de email tiene id="checkout-email" pero NO tiene data-testid.
 * El botón "Continuar al Pago" tampoco tiene data-testid ni id;
 * se localiza por su texto visible con XPath.
 * El botón está disabled hasta que el usuario ingresa un email válido.
 */
public class CheckoutPage {

    /**
     * Campo de correo electrónico del invitado.
     * HTML real: &lt;input id="checkout-email" type="email"&gt;
     */
    public static final Target EMAIL_INPUT =
            Target.the("campo email del invitado")
                  .locatedBy("#checkout-email");

    /**
     * Botón para continuar al paso de pago.
     * HTML real: &lt;button&gt;Continuar al Pago&lt;/button&gt; (sin data-testid).
     * Se habilita automáticamente al ingresar un email válido.
     */
    public static final Target CONTINUE_BTN =
            Target.the("botón continuar al pago")
                  .locatedBy("//button[normalize-space()='Continuar al Pago']");
}
