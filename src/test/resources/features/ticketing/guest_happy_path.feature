# =============================================================================
# Feature: Compra exitosa de entrada como invitado (Guest Happy Path)
# Proyecto: Ticketing MVP
# Cubre: HU-01 – Visualización de cartelera / HU-02 – Selección de función
#         HU-06 – Flujo de compra guest
# Tags utilizados: @guest-happy-path  @smoke  @mvp
# =============================================================================
@mvp @guest-happy-path
Feature: Compra exitosa de entrada como invitado

  Como visitante anónimo del sitio
  Quiero poder comprar una entrada para una obra de teatro sin registrarme
  Para poder asistir al evento de forma rápida y sin fricciones

  Background:
    Given que Marta es un invitado que desea comprar una entrada

  @smoke
  Scenario: Invitado completa el flujo de compra exitosamente de principio a fin
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el primer evento disponible
    And Marta elige el tier "GENERAL"
    And Marta reserva su lugar
    And Marta ingresa su correo "marta.guest@example.com"
    And Marta continúa hacia el pago
    And Marta completa el pago simulado exitosamente
    Then Marta debería ver la pantalla de confirmación de compra

  Scenario Outline: Invitado puede elegir distintos tiers de precio
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el primer evento disponible
    And Marta elige el tier "<tier>"
    And Marta reserva su lugar
    And Marta ingresa su correo "marta.guest@example.com"
    And Marta continúa hacia el pago
    And Marta completa el pago simulado exitosamente
    Then Marta debería ver la pantalla de confirmación de compra

    Examples:
      | tier         |
      | VIP          |
      | GENERAL      |
      | EARLY_BIRD   |
