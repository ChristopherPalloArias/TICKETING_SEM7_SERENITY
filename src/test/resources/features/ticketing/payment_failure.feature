# =============================================================================
# Feature: Pago rechazado en flujo de compra guest
# Proyecto: Ticketing MVP
# Cubre: HU-06 – Flujo de compra guest (camino alternativo: pago declinado)
# Tags: @payment-failure  @mvp
# =============================================================================
@mvp @payment-failure
Feature: Pago rechazado en flujo de compra guest

  Como visitante anónimo del sitio
  Cuando intento pagar y el pago es rechazado
  Quiero ver una pantalla de error clara con la opción de reintentar
  Para poder corregir la situación sin perder mi reserva

  Background:
    Given que Marta es un invitado que desea comprar una entrada

  @smoke
  Scenario: Invitado intenta pagar pero el pago es rechazado y el sistema muestra la pantalla de fallo con opción de reintento
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento "The Phantom's Echo"
    And Marta elige el tier "GENERAL"
    And Marta reserva su lugar
    And Marta ingresa su correo "marta.guest@example.com"
    And Marta continúa hacia el pago
    And Marta simula un pago rechazado
    Then Marta debería ver la pantalla de pago rechazado
    And Marta debería ver la opción de reintentar el pago
