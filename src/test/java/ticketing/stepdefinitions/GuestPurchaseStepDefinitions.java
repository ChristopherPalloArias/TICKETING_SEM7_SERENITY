package ticketing.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import ticketing.navigation.NavigateTo;
import ticketing.screenplay.questions.SuccessScreen;
import ticketing.screenplay.tasks.CompletePayment;
import ticketing.screenplay.tasks.ContinueToPayment;
import ticketing.screenplay.tasks.EnterEmail;
import ticketing.screenplay.tasks.ReserveTicket;
import ticketing.screenplay.tasks.SelectEvent;
import ticketing.screenplay.tasks.SelectTier;
import ticketing.screenplay.ui.ConfirmationPage;

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
        actor.attemptsTo(
                EnterEmail.withAddress(email)
        );
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
                Ensure.that(ConfirmationPage.SUCCESS_TITLE).isDisplayed()
        );
    }
}
