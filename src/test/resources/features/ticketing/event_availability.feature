# =============================================================================
# Feature: Disponibilidad visible del evento y sus tiers
# Proyecto: Ticketing MVP
# Cubre: HU-03 – Disponibilidad de eventos y tiers
# Tags: @event-availability  @mvp
#
# Todos los escenarios son ejecutables. Datos verificados en DB (2026-04-08):
#   - "BODAS DE SANGRE"       → tier VIP        quota=0        → AGOTADO
#   - "The Phantom's Echo"    → tier EARLY_BIRD  valid_until<NOW → AGOTADO
#   Condiciones persistidas en: V16__create_test_scenario_conditions.sql
#
# Locator auditado en TicketTier.tsx:
#   data-testid="tier-{tierType}"  /  aria-disabled="true" cuando !isAvailable
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

  @requires-seed-data
  Scenario: Comprador observa un tier agotado como no disponible en pantalla
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "BODAS DE SANGRE"
    Then Marta debería ver el tier "VIP" marcado como no disponible
    And Marta no debería poder seleccionar el tier "VIP" para comprar

  @requires-seed-data
  Scenario: Comprador no ve el Early Bird como opción de compra activa cuando su período venció
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "The Phantom's Echo"
    Then Marta no debería ver el tier "EARLY_BIRD" disponible para selección
