# =============================================================================
# Feature: Visibilidad del ticket confirmado tras el proceso de compra
# Proyecto: Ticketing MVP
# Cubre: HU-06 – Flujo de compra guest (evidencia de ticket emitido)
# Tags: @ticket-visibility  @mvp
#
# Escenarios:
#   1. Invitado visualiza el ticket confirmado tras una compra exitosa
#   2. Invitado con pago rechazado no visualiza el ticket confirmado
#
# Prerrequisito structural:
#   - No duplica pasos ya validados en guest_happy_path.feature ni en
#     payment_failure.feature. El foco es la validación explícita de la
#     presencia o ausencia del ticket como evidencia de compra.
# =============================================================================
@mvp @ticket-visibility
Feature: Visibilidad del ticket confirmado tras el proceso de compra

  Como visitante anónimo del sitio
  Quiero ver mi ticket de compra confirmado cuando el pago fue aprobado
  Y no ver ningún ticket cuando el pago fue rechazado
  Para tener certeza clara del resultado de mi transacción

  Background:
    Given que Marta es un invitado que desea comprar una entrada

  @smoke
  Scenario: Invitado visualiza el ticket confirmado tras una compra exitosa
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "The Phantom's Echo"
    And Marta elige el tier "GENERAL"
    And Marta reserva su lugar
    And Marta ingresa su correo "marta.guest@example.com"
    And Marta continúa hacia el pago
    And Marta completa el pago simulado exitosamente
    Then Marta debería ver el ticket de compra confirmado
    And Marta debería ver la información principal del ticket en pantalla

  Scenario: Invitado con pago rechazado no visualiza el ticket confirmado
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "The Phantom's Echo"
    And Marta elige el tier "GENERAL"
    And Marta reserva su lugar
    And Marta ingresa su correo "marta.guest@example.com"
    And Marta continúa hacia el pago
    And Marta simula un pago rechazado
    Then Marta debería ver la pantalla de pago rechazado
    And Marta no debería ver ningún ticket confirmado
