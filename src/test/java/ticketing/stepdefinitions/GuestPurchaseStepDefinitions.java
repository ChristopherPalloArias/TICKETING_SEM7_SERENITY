package ticketing.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.navigation.NavigateTo;
import ticketing.screenplay.questions.SuccessScreen;
import ticketing.screenplay.questions.TicketScreen;
import ticketing.screenplay.tasks.CompletePayment;
import ticketing.screenplay.tasks.ContinueToPayment;
import ticketing.screenplay.tasks.EnterEmail;
import ticketing.screenplay.tasks.ReserveTicket;
import ticketing.screenplay.tasks.SelectEvent;
import ticketing.screenplay.tasks.SelectTier;
import ticketing.screenplay.ui.ConfirmationPage;
import ticketing.screenplay.ui.FailedPaymentPage;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Definiciones de pasos Cucumber para el Guest Happy Path.
 *
 * Feature cubierta: features/ticketing/guest_happy_path.feature
 * Flujo: /eventos → evento → tier → reservar → email → pago → confirmación
 *
 * ──────────────────────────────────────────────────────────────────────────
 * NOTA SOBRE ESPERAS:
 * No se usan Thread.sleep(). Todas las esperas son explícitas mediante
 * WaitUntil dentro de cada tarea Screenplay.
 * ──────────────────────────────────────────────────────────────────────────
 */
public class GuestPurchaseStepDefinitions {

    // ──────────────────────────────────────────────────────────────────────
    // Background
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Paso de contexto: introduce al actor como invitado.
     * El actor ya fue creado por ParameterDefinitions.setTheStage().
     * Este paso es descriptivo y no requiere acción de WebDriver.
     *
     * @param actor el actor Marta (o cualquier nombre dado en el feature)
     */
    @Given("que {actor} es un invitado que desea comprar una entrada")
    public void esUnInvitado(Actor actor) {
        // El actor ya está configurado en el escenario por ParameterDefinitions.
        // Este paso establece el contexto narrativo del test.
        actor.remember("rol", "invitado");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Navegación
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor navega a la cartelera de eventos (/eventos).
     *
     * @param actor el actor
     */
    @Given("que {actor} navega a la cartelera de eventos")
    public void navegaALaCartelera(Actor actor) {
        actor.attemptsTo(
                NavigateTo.laCartelera()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Selección de evento
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor hace clic en el primer evento visible de la cartelera.
     *
     * @param actor el actor
     */
    @When("{actor} selecciona el primer evento disponible")
    public void seleccionaElPrimerEvento(Actor actor) {
        actor.attemptsTo(
                SelectEvent.elPrimeroDisponible()
        );
    }

    /**
     * El actor hace clic en la tarjeta del evento indicado por su título.
     * Usa el aria-label "Ver {titulo}" que el frontend asigna a cada tarjeta.
     *
     * @param actor  el actor
     * @param titulo título exacto del evento, ej. "The Phantom's Echo"
     */
    @When("{actor} selecciona el evento {string}")
    public void seleccionaElEvento(Actor actor, String titulo) {
        actor.attemptsTo(
                SelectEvent.porTitulo(titulo)
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Selección de tier
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor elige el tier de precio indicado.
     *
     * Valores válidos definidos en el MVP: "VIP", "GENERAL", "EARLY_BIRD".
     *
     * @param actor    el actor
     * @param tierName nombre del tier tal como aparece en el feature
     */
    @And("{actor} elige el tier {string}")
    public void eligeElTier(Actor actor, String tierName) {
        actor.attemptsTo(
                SelectTier.called(tierName)
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Reserva
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor hace clic en el botón de reserva.
     *
     * @param actor el actor
     */
    @And("{actor} reserva su lugar")
    public void reservaSuLugar(Actor actor) {
        actor.attemptsTo(
                ReserveTicket.now()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Checkout – ingreso de email
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor ingresa su correo electrónico en el campo de checkout.
     *
     * @param actor el actor
     * @param email dirección de correo a ingresar
     */
    @And("{actor} ingresa su correo {string}")
    public void ingresaSuCorreo(Actor actor, String email) {
        // Añade timestamp al local-part para garantizar idempotencia entre corridas.
        // Ej: "marta.guest@example.com" → "marta.guest+20260408073749@example.com"
        String uniqueEmail = toUniqueEmail(email);
        actor.attemptsTo(
                EnterEmail.withAddress(uniqueEmail)
        );
    }

    /**
     * Inserta un sufijo de timestamp (yyyyMMddHHmmss) antes del '@' del email.
     * Garantiza unicidad en cada corrida sin modificar el dominio.
     *
     * @param email email base, ej. "marta.guest@example.com"
     * @return email único, ej. "marta.guest+20260408073749@example.com"
     */
    private static String toUniqueEmail(String email) {
        String ts = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
                .format(new java.util.Date());
        int at = email.indexOf('@');
        if (at < 0) return email + "+" + ts;
        return email.substring(0, at) + "+" + ts + email.substring(at);
    }

    // ──────────────────────────────────────────────────────────────────────
    // Continuar al pago
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor hace clic en "Continuar al pago".
     *
     * @param actor el actor
     */
    @And("{actor} continúa hacia el pago")
    public void continuaHaciaElPago(Actor actor) {
        actor.attemptsTo(
                ContinueToPayment.now()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Pago mock E2E
    // ──────────────────────────────────────────────────────────────────────

    /**
     * El actor simula un pago exitoso usando el botón mock de E2E.
     * Requiere que el frontend esté corriendo con: npm run dev:e2e
     *
     * @param actor el actor
     */
    @And("{actor} completa el pago simulado exitosamente")
    public void completaElPagoSimulado(Actor actor) {
        actor.attemptsTo(
                CompletePayment.successfully()
        );
    }

    /**
     * El actor hace clic en "Simular Pago Rechazado".
     * Reutiliza CompletePayment.withFailure() — mismo botón de la pantalla mock.
     *
     * @param actor el actor
     */
    @And("{actor} simula un pago rechazado")
    public void simulaUnPagoRechazado(Actor actor) {
        actor.attemptsTo(
                CompletePayment.withFailure()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Validación de pantalla de confirmación
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Valida que el actor ve la pantalla de confirmación de compra exitosa.
     *
     * Verifica que el elemento [data-testid="success-title"] sea visible.
     *
     * @param actor el actor
     */
    @Then("{actor} debería ver la pantalla de confirmación de compra")
    public void deberiaVerLaConfirmacion(Actor actor) {
        actor.attemptsTo(
                WaitUntil.the(ConfirmationPage.SUCCESS_TITLE, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(ConfirmationPage.SUCCESS_TITLE).isDisplayed()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Validación de pantalla de pago rechazado
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Valida que el actor ve la pantalla de pago rechazado.
     * Comprueba el texto "Pago declinado." con espera explícita.
     *
     * @param actor el actor
     */
    @Then("{actor} debería ver la pantalla de pago rechazado")
    public void deberiaVerLaPantallaDeRechazo(Actor actor) {
        actor.attemptsTo(
                WaitUntil.the(FailedPaymentPage.FAILURE_TITLE, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(FailedPaymentPage.FAILURE_TITLE).isDisplayed()
        );
    }

    /**
     * Valida que el botón de reintento está disponible en la pantalla de rechazo.
     * No hace clic — solo valida su presencia y visibilidad.
     *
     * @param actor el actor
     */
    @And("{actor} debería ver la opción de reintentar el pago")
    public void deberiaVerLaOpcionDeReintentar(Actor actor) {
        actor.attemptsTo(
                WaitUntil.the(FailedPaymentPage.RETRY_BTN, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(FailedPaymentPage.RETRY_BTN).isDisplayed()
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // Visibilidad del ticket confirmado
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Valida que el ticket de compra confirmado es visible.
     *
     * Usa WaitUntil como espera explícita antes de la assertion.
     * Cubre el requisito: "invitado visualiza el ticket confirmado tras compra exitosa".
     *
     * @param actor el actor
     */
    @Then("{actor} debería ver el ticket de compra confirmado")
    public void deberiaVerElTicketConfirmado(Actor actor) {
        actor.attemptsTo(
                WaitUntil.the(ConfirmationPage.SUCCESS_TITLE, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(TicketScreen.isConfirmed()).isTrue()
        );
    }

    /**
     * Valida que la información principal del ticket está presente en pantalla.
     *
     * Comprueba el texto exacto del título de confirmación emitido por el frontend.
     * No requiere espera adicional: el paso anterior garantiza que el elemento
     * ya es visible antes de esta validación.
     *
     * @param actor el actor
     */
    @And("{actor} debería ver la información principal del ticket en pantalla")
    public void deberiaVerLaInformacionDelTicket(Actor actor) {
        actor.attemptsTo(
                Ensure.that(TicketScreen.confirmationTitle())
                      .isEqualTo("¡Pago aprobado!")
        );
    }

    /**
     * Valida que NO se muestre ningún ticket de compra confirmado.
     *
     * Escenario negativo: tras un pago rechazado el usuario permanece en la
     * pantalla de fallo, que fue validada en el paso anterior. Este paso
     * confirma explícitamente la ausencia de evidencia de compra emitida.
     *
     * Implementación defensiva en {@link TicketScreen#isAbsent()}: si el
     * elemento no existe en el DOM, se retorna true sin lanzar excepción.
     *
     * @param actor el actor
     */
    @And("{actor} no debería ver ningún ticket confirmado")
    public void noDeberiaVerNingunTicketConfirmado(Actor actor) {
        actor.attemptsTo(
                Ensure.that(TicketScreen.isAbsent()).isTrue()
        );
    }
}
