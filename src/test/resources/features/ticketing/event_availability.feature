# =============================================================================
# Feature: Disponibilidad visible del evento y sus tiers
# Proyecto: Ticketing MVP
# Cubre: HU-03 – Disponibilidad de eventos y tiers
# Tags: @event-availability  @mvp
#
# ── Estado de automatización ──────────────────────────────────────────────
#
# EJECUTABLE HOY:
#   @smoke – La cartelera muestra eventos disponibles
#     Verifica que la cartelera renderiza al menos una tarjeta de evento.
#     Sin dependencia de datos específicos: funciona con cualquier evento
#     publicado en el seed data del entorno.
#
# @wip – BLOQUEADOS POR DEPENDENCIA DE DATOS / DOM:
#
#   "Tier agotado" – PRECONDICIONES FALTANTES:
#     1. Seed data con un evento que tenga stock = 0 en al menos un tier.
#     2. Auditoría del DOM para identificar el atributo/clase que el frontend
#        usa para el estado deshabilitado del tier card:
#        (ej. aria-disabled="true", class "sold-out", button[disabled], etc.)
#     Acción requerida: inspeccionar el DOM en devtools con ese estado activo
#     y actualizar EventDetailPage con el selector correcto.
#
#   "Early Bird vencido" – PRECONDICIONES FALTANTES:
#     1. Seed data con un evento cuyo early_bird_expiry < fecha actual.
#     2. Auditoría del DOM para verificar si el frontend oculta el tier,
#        lo deshabilita visualmente o muestra un badge "Vencido".
#     Acción requerida: igual que el caso anterior.
#
# Para ejecutar SOLO los @wip cuando los datos estén disponibles:
#   mvn clean test -Dcucumber.filter.tags="@wip" \
#                  -Dwebdriver.base.url=http://localhost:5173
# =============================================================================
@mvp @event-availability
Feature: Disponibilidad visible del evento y sus tiers

  Como visitante anónimo del sitio
  Quiero ver los eventos disponibles en la cartelera con sus tiers correctamente presentados
  Para poder tomar decisiones de compra informado sobre disponibilidad y precio

  Background:
    Given que Marta es un invitado que desea comprar una entrada

  # ── EJECUTABLE – sin dependencia de datos específicos ────────────────────
  @smoke
  Scenario: La cartelera muestra al menos un evento disponible
    Given que Marta navega a la cartelera de eventos
    Then Marta debería ver al menos un evento disponible en la cartelera

  # ── @wip – Bloqueado: requiere seed data con stock=0 + auditoría de DOM ─
  @wip
  Scenario: Tier agotado aparece como no disponible en la pantalla del evento
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento que tiene un tier agotado
    Then Marta debería ver ese tier marcado como no disponible
    And Marta no debería poder seleccionar el tier agotado

  # ── @wip – Bloqueado: requiere seed data con early_bird_expiry pasado ───
  @wip
  Scenario: Tier Early Bird vencido no aparece como opción de compra activa
    Given que Marta navega a la cartelera de eventos
    When Marta selecciona el evento con Early Bird vencido
    Then Marta no debería ver el Early Bird como tier activo de compra
