package ticketing.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.waits.WaitUntil;
import ticketing.navigation.NavigateTo;
import ticketing.screenplay.questions.EventsListScreen;
import ticketing.screenplay.questions.SuccessScreen;
import ticketing.screenplay.questions.TicketScreen;
import ticketing.screenplay.questions.TierAvailabilityScreen;
import ticketing.screenplay.tasks.CompletePayment;
import ticketing.screenplay.tasks.ContinueToPayment;
import ticketing.screenplay.tasks.EnterEmail;
import ticketing.screenplay.tasks.ReserveTicket;
import ticketing.screenplay.tasks.SelectEvent;
import ticketing.screenplay.tasks.SelectTier;
import ticketing.screenplay.ui.ConfirmationPage;
import ticketing.screenplay.ui.EventDetailPage;
import ticketing.screenplay.ui.EventsPage;
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

    // ──────────────────────────────────────────────────────────────────────
    // Disponibilidad de la cartelera (HU-03)
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Valida que la cartelera muestra al menos un evento disponible.
     *
     * Usa WaitUntil sobre EventsPage.PRIMER_EVENTO como espera explícita,
     * luego verifica con EventsListScreen.hasAtLeastOneEvent() que la
     * colección de tarjetas de evento no está vacía.
     *
     * Cobertura: HU-03 – escenario positivo de visibilidad de cartelera.
     *
     * @param actor el actor
     */
    @Then("{actor} debería ver al menos un evento disponible en la cartelera")
    public void deberiaVerAlMenosUnEventoEnCartelera(Actor actor) {
        actor.attemptsTo(
                WaitUntil.the(EventsPage.PRIMER_EVENTO, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(EventsListScreen.hasAtLeastOneEvent()).isTrue(),
                Ensure.that(EventsListScreen.visibleEventCount()).isGreaterThan(0)
        );
    }

    // ──────────────────────────────────────────────────────────────────────
    // @wip – Stubs pendientes de datos controlados (HU-03)
    // ──────────────────────────────────────────────────────────────────────
    // Estos steps NO se ejecutan en el build estándar porque los escenarios
    // que los referencian llevan la etiqueta @wip, filtrada por defecto en
    // junit-platform.properties.
    //
    // Para activarlos cuando los datos estén disponibles:
    //   mvn clean test -Dcucumber.filter.tags="@wip"
    // ──────────────────────────────────────────────────────────────────────

    /**
     * [WIP] Navega al evento que en seed data tiene un tier con stock = 0.
     *
     * PENDIENTE: Reemplazar el título hardcodeado por el nombre real del
     * evento con tier agotado una vez que el seed data esté controlado.
     * Auditar el DOM para identificar el selector del estado "agotado".
     *
     * @param actor el actor
     */
    @When("{actor} selecciona el evento que tiene un tier agotado")
    public void seleccionaElEventoConTierAgotado(Actor actor) {
        // TODO: reemplazar cuando seed data garantice un evento con stock=0
        // actor.attemptsTo(SelectEvent.porTitulo("<nombre-evento-agotado>"));
        actor.remember("wip", "tier-agotado");
    }

    /**
     * [WIP] Valida que el tier agotado se muestra visualmente como no disponible.
     *
     * PENDIENTE: Auditar el DOM del frontend con un evento agotado para determinar
     * si el estado se renderiza via aria-disabled="true", clase CSS, botón disabled,
     * o badge de texto. Luego actualizar EventDetailPage con el Target correcto.
     *
     * @param actor el actor
     */
    @Then("{actor} debería ver ese tier marcado como no disponible")
    public void deberiaVerElTierMarcadoComoNoDisponible(Actor actor) {
        // TODO: implementar con selector auditado de estado "agotado"
        actor.remember("wip", "tier-agotado-visible");
    }

    /**
     * [WIP] Valida que el tier agotado no puede ser seleccionado.
     *
     * PENDIENTE: Depende del mismo selector que el paso anterior.
     *
     * @param actor el actor
     */
    @And("{actor} no debería poder seleccionar el tier agotado")
    public void noDeberiaPoderSeleccionarElTierAgotado(Actor actor) {
        // TODO: implementar assertion de no-interaccionabilidad
        actor.remember("wip", "tier-agotado-no-clickeable");
    }

    /**
     * [WIP] Navega al evento que en seed data tiene Early Bird con fecha vencida.
     *
     * PENDIENTE: Reemplazar por nombre real del evento con early_bird_expiry pasado.
     * Verificar que el backend/frontend lo marca como inactivo correctamente.
     *
     * @param actor el actor
     */
    @When("{actor} selecciona el evento con Early Bird vencido")
    public void seleccionaElEventoConEarlyBirdVencido(Actor actor) {
        // TODO: reemplazar cuando seed data garantice early_bird_expiry < hoy
        actor.remember("wip", "early-bird-vencido");
    }

    /**
     * [WIP] Valida que el tier Early Bird no aparece activo cuando su fecha venció.
     *
     * PENDIENTE: Auditar si el frontend oculta el tier, lo deshabilita o
     * muestra un badge "Vencido". Actualizar EventDetailPage con el Target.
     *
     * @param actor el actor
     */
    @Then("{actor} no debería ver el Early Bird como tier activo de compra")
    public void noDeberiaVerElEarlyBirdComoTierActivo(Actor actor) {
        // TODO: implementar con selector auditado de estado "vencido"
        actor.remember("wip", "early-bird-inactivo");
    }

    // ──────────────────────────────────────────────────────────────────────
    // HU-03 – Disponibilidad de tiers (@requires-seed-data)
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Valida que un tier se muestra visualmente como no disponible (deshabilitado).
     *
     * Espera explícita usando el selector base del tier (que encuentra el elemento
     * aunque esté deshabilitado, ya que aria-disabled no afecta la visibilidad CSS).
     * La assertion delega en TierAvailabilityScreen.isTierUnavailable() que verifica
     * la presencia del atributo aria-disabled="true" en el tier card.
     *
     * Supuesto DOM verificar antes de activar: aria-disabled="true" en div[@role='button'].
     * Si el selector no coincide, actualizar EventDetailPage.tierDeshabilitado().
     *
     * @param actor    el actor
     * @param tierName nombre técnico del tier ("GENERAL", "VIP", "EARLY_BIRD")
     */
    @Then("{actor} debería ver el tier {string} marcado como no disponible")
    public void deberiaVerElTierComoNoDisponible(Actor actor, String tierName) {
        actor.attemptsTo(
                WaitUntil.the(EventDetailPage.tierButton(tierName), isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(TierAvailabilityScreen.isTierUnavailable(tierName)).isTrue()
        );
    }

    /**
     * Valida que el tier sigue en estado deshabilitado (no se habilitó como efecto
     * colateral de la espera del paso anterior).
     *
     * Complementa a {@link #deberiaVerElTierComoNoDisponible} confirmando que el
     * estado no disponible persiste. No intenta hacer clic en el tier.
     *
     * @param actor    el actor
     * @param tierName nombre técnico del tier ("GENERAL", "VIP", "EARLY_BIRD")
     */
    @And("{actor} no debería poder seleccionar el tier {string} para comprar")
    public void noDeberiaPoderSeleccionarElTierParaComprar(Actor actor, String tierName) {
        // La assertion anterior ya verificó el estado. Este paso confirma
        // que el estado "no disponible" persiste sin necesitar nueva espera.
        actor.attemptsTo(
                Ensure.that(TierAvailabilityScreen.isTierUnavailable(tierName)).isTrue()
        );
    }

    /**
     * Valida que un tier no está disponible para selección, ya sea porque está
     * deshabilitado (aria-disabled="true") o porque está ausente del DOM.
     *
     * Cubre ambas variantes posibles de comportamiento del frontend para tiers
     * no activos (p.ej. Early Bird vencido):
     *   - El tier se muestra pero con aria-disabled="true"
     *   - El tier se oculta completamente del DOM
     *
     * La espera explícita usa TIER_GENERAL como proxy de "página de detalle cargada",
     * ya que cualquier evento activo expone al menos un tier habilitado.
     *
     * @param actor    el actor
     * @param tierName nombre técnico del tier ("GENERAL", "VIP", "EARLY_BIRD")
     */
    @Then("{actor} no debería ver el tier {string} disponible para selección")
    public void noDeberiaVerElTierDisponibleParaSeleccion(Actor actor, String tierName) {
        actor.attemptsTo(
                WaitUntil.the(EventDetailPage.TIER_GENERAL, isVisible())
                         .forNoMoreThan(10).seconds(),
                Ensure.that(TierAvailabilityScreen.isTierUnavailableOrAbsent(tierName)).isTrue()
        );
    }
}
