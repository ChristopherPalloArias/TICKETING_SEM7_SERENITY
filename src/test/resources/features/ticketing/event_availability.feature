# =============================================================================
# Feature: Disponibilidad visible del evento y sus tiers
# Proyecto: Ticketing MVP
# Cubre: HU-03 – Disponibilidad de eventos y tiers
# Tags: @event-availability  @mvp
#
# Estado de automatización:
#   @smoke – La cartelera muestra eventos disponibles → EJECUTABLE
#
# Escenarios pendientes de datos controlados (documentados fuera de la suite):
#   - Tier agotado visible como no disponible
#     Precondición: seed data con stock=0 + auditoría DOM del estado deshabilitado
#   - Early Bird vencido no activo
#     Precondición: seed data con early_bird_expiry < hoy + auditoría DOM
#   Acción: inspeccionar el DOM en devtools con esos estados activos,
#   actualizar EventDetailPage, agregar los escenarios y sus steps.
# =============================================================================
@mvp @event-availability
Feature: Disponibilidad visible del evento y sus tiers

  Como visitante anónimo del sitio
  Quiero ver los eventos disponibles en la cartelera con sus tiers correctamente presentados
  Para poder tomar decisiones de compra informado sobre disponibilidad y precio

  Background:
    Given que Marta es un invitado que desea comprar una entrada

  @smoke
  Scenario: La cartelera muestra al menos un evento disponible
    Given que Marta navega a la cartelera de eventos
    Then Marta debería ver al menos un evento disponible en la cartelera

  # ── @requires-seed-data ───────────────────────────────────────────────────────
  # Estos 2 escenarios requieren precondiciones de datos controlados en el backend
  # y una auditoría del DOM del frontend con esos estados activos.
  #
  # Precondición escenario 1:
  #   - Seed data: evento con título "Obra con Tier Agotado" y stock=0 en GENERAL
  #   - DOM verificado: div[@role='button'][@aria-disabled='true'] en tier card
  #   - REEMPLAZAR el título del evento por el nombre real en seed data.
  #
  # Precondición escenario 2:
  #   - Seed data: evento con título "Obra con Early Bird Vencido" y
  #     early_bird_expiry < fecha actual
  #   - DOM verificado: tier Early Bird ausente del DOM o con aria-disabled="true"
  #   - REEMPLAZAR el título del evento por el nombre real en seed data.
  #
  # Estos escenarios se filtran del build estándar via:
  #   cucumber.filter.tags=not @requires-seed-data
  #
  # Para ejecutarlos cuando los datos estén listos:
  #   mvn clean test -Dcucumber.filter.tags="@requires-seed-data" \
  #                  -Dwebdriver.base.url=http://localhost:5173
  # ─────────────────────────────────────────────────────────────────────────────

  @requires-seed-data
  Scenario: Comprador observa un tier agotado como no disponible en pantalla
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "Obra con Tier Agotado"
    Then Marta debería ver el tier "GENERAL" marcado como no disponible
    And Marta no debería poder seleccionar el tier "GENERAL" para comprar

  @requires-seed-data
  Scenario: Comprador no ve el Early Bird como opción de compra activa cuando su período venció
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "Obra con Early Bird Vencido"
    Then Marta no debería ver el tier "EARLY_BIRD" disponible para selección
