# Plan de Pruebas — Ticketing MVP

> **Proyecto:** Ticketing MVP — Sistema de venta de entradas de teatro  
> **Versión del plan:** 1.0  
> **Fecha:** 2026-04-08  
> **Autor:** Equipo QA

---

## Índice

1. [Objetivo](#1-objetivo)
2. [Alcance](#2-alcance)
3. [Entorno de ejecución](#3-entorno-de-ejecución)
4. [Estrategia de automatización](#4-estrategia-de-automatización)
   - 4.1 [Tipos de prueba y herramientas](#41-tipos-de-prueba-y-herramientas)
   - 4.2 [Principio de diseño: automatización por capas](#42-principio-de-diseño-automatización-por-capas)
   - 4.3 [Criterio de alto ROI](#43-criterio-de-alto-roi)
   - 4.4 [Matriz de cobertura por historia de usuario](#44-matriz-de-cobertura-por-historia-de-usuario)
5. [Resultados de ejecución](#5-resultados-de-ejecución)
6. [Escenarios fuera del alcance de la capa UI](#6-escenarios-fuera-del-alcance-de-la-capa-ui)
7. [Riesgos y mitigaciones](#7-riesgos-y-mitigaciones)

---

## 1. Objetivo

Documentar la estrategia de calidad ejecutada para el Ticketing MVP,
describiendo qué se prueba, en qué capa, con qué herramienta y con qué justificación.

El plan cubre tres capas de automatización complementarias y no redundantes:
**automatización funcional UI** (Serenity BDD), **pruebas de API y lógica de negocio** (Karate DSL)
y **pruebas de rendimiento** (k6), más la evidencia de consistencia de datos (SQL).

---

## 2. Alcance

### En alcance

- Flujos del comprador anónimo (guest) en la interfaz web.
- Reglas de negocio del ciclo de reserva y pago:
  disponibilidad de tiers, creación de reserva, expiración, pago, cancelación, notificación.
- Rendimiento bajo carga concurrente del API.
- Consistencia de datos en la base de datos post-transacción.

### Fuera de alcance

- Pruebas de seguridad (penetration, OWASP).
- Pruebas de accesibilidad WCAG.
- Pruebas en dispositivos móviles o browsers distintos de Chrome.
- Flujos del panel de administración (cubiertos en Karate como contratos de API).

---

## 3. Entorno de ejecución

| Componente           | Detalle                                              |
|----------------------|------------------------------------------------------|
| Frontend             | Vite + React SPA — Docker Compose en `http://localhost:5173` (estándar). Alternativa local: `npm run dev:e2e`. |
| API Gateway          | Spring Boot — `localhost:8080`                       |
| ms-events            | Spring Boot — `localhost:8081` / Docker container    |
| ms-ticketing         | Spring Boot — `localhost:8082` / Docker container    |
| ms-notifications     | Spring Boot — `localhost:8083` / Docker container    |
| Base de datos eventos | PostgreSQL `localhost:5433` (`events_db`)            |
| Base de datos tickets | PostgreSQL `localhost:5434` (`ticketing_db`)         |
| Message broker       | RabbitMQ — `localhost:5672`                          |
| Navegador (UI tests) | Google Chrome (ChromeDriver gestionado por WebDriverManager) |
| JDK                  | Java 17                                              |

### Datos de prueba

Los datos base se cargan a través de migraciones Flyway en `ms-events`:

- `V7__seed_demo_data.sql` — 4 eventos PUBLISHED, 9 tiers iniciales.
- `V16__create_test_scenario_conditions.sql` — condiciones de HU-03:
  - BODAS DE SANGRE · VIP: `quota = 0` (tier AGOTADO).
  - The Phantom's Echo · EARLY_BIRD: `valid_until` en el pasado (tier VENCIDO).

---

## 4. Estrategia de automatización

### 4.1 Tipos de prueba y herramientas

| Capa                  | Herramienta     | Alcance                                                                 | Repositorio       |
|-----------------------|-----------------|-------------------------------------------------------------------------|-------------------|
| **Funcional UI**      | Serenity BDD    | Flujos del comprador visibles en el navegador                           | *este repo*       |
| **API / Lógica de negocio** | Karate DSL | Contratos, reglas de negocio, estados, concurrencia, expiración, notificaciones | repo separado |
| **Rendimiento**       | k6              | Carga concurrente sobre endpoints críticos de reserva y pago            | repo separado     |
| **Consistencia de datos** | SQL (evidencia) | Verificación de estado en DB tras el ciclo completo                  | repo separado     |

#### Serenity BDD — Funcional UI

Ejercita la interfaz del usuario desde Chrome, validando los flujos del comprador
que solo son observables y verificables desde el navegador:

- Cartelera con al menos un evento disponible.
- Tier agotado marcado como `aria-disabled` en el card de selección.
- Early Bird vencido no disponible para selección.
- Flujo completo de compra exitosa hasta pantalla de confirmación.
- Flujo de pago rechazado con pantalla de error y botón de reintento.
- Presencia del ticket confirmado tras pago exitoso.
- Ausencia de ticket en pantalla de pago fallido.

**Stack:** Java · Serenity BDD 5.3.8 · Cucumber JVM 7.34.2 · JUnit Platform 6.0.3 · Maven · Screenplay Pattern.

**Comando de ejecución (oficial):**
```bash
mvn clean test
```

> URL base por defecto: `http://localhost:5173`. Ejecución validada contra el frontend Docker. No se requieren parámetros adicionales para el flujo estándar.

#### Karate DSL — API y lógica de negocio

Cubre los 29 casos del ciclo MVP a nivel de API REST:

- Contratos de respuesta de cada endpoint (status codes, schemas JSON).
- Reglas de negocio: quota, validFrom/validUntil, límite de reintentos de pago.
- Estados de reserva: PENDING → CONFIRMED, PAYMENT_FAILED, EXPIRED.
- Concurrencia: múltiples compradores sobre el mismo tier con quota limitada.
- Expiración: scheduler que libera reservas PENDING caducadas y restaura quota.
- Liberación de cupo post-expiración verificada como incremento de quota.
- Notificaciones: eventos publicados en RabbitMQ ante pago exitoso, fallido y expiración.
- Flujos de autenticación (login, token, perfil protegido).
- Historial de compras del usuario autenticado.
- Administración de eventos (creación, publicación, cancelación).

#### k6 — Rendimiento

- Escenario de carga: N usuarios simultáneos completando el flujo reserva/pago.
- Métricas: latencia p95, tasa de errores, throughput bajo carga sostenida.
- Evento especial: *k6 Performance Load Test* insertado en seed data para simular
  condiciones de alta demanda con `quota = 10 000` tiers.

#### SQL — Consistencia de datos

Consultas de verificación post ejecución sobre `events_db` y `ticketing_db`:

- Quota de tier decrementada correctamente tras cada reserva confirmada.
- Quota restaurada tras expiración de reserva.
- Estado de ticket `CONFIRMED` solo cuando el pago fue `APPROVED`.
- Sin reservas en estado `PENDING` más antiguas que el TTL (10 min).

---

### 4.2 Principio de diseño: automatización por capas

La estrategia se diseñó conscientemente para **no duplicar cobertura entre capas**:

```
┌──────────────────────────────────────────────────────────────────┐
│  UI / E2E (Serenity)                                             │
│  ── Lo que solo el navegador puede ver ──────────────────────   │
│  Flujos del guest, estados visuales (disabled/absent),          │
│  pantallas de confirmación y rechazo, ticket renderizado.       │
├──────────────────────────────────────────────────────────────────┤
│  API / Integración (Karate)                                      │
│  ── Lógica de negocio, contratos, estados internos ────────── │
│  29 casos: quota, expiración, reintentos, concurrencia,         │
│  notificaciones, autenticación, admin, historial.               │
├──────────────────────────────────────────────────────────────────┤
│  Rendimiento (k6)                                                │
│  ── Estabilidad bajo carga ──────────────────────────────────  │
│  Carga concurrente, latencia p95, saturación del pool.          │
├──────────────────────────────────────────────────────────────────┤
│  Datos (SQL)                                                     │
│  ── Evidencia de consistencia ───────────────────────────────  │
│  Verificación directa en DB de estado esperado.                 │
└──────────────────────────────────────────────────────────────────┘
```

Cada capa prueba lo que hace mejor. Los criterios que requieren inspección del DOM
se prueban solo en Serenity. Los que requieren lógica de estado, temporización o
múltiples actores se prueban en Karate o k6.

---

### 4.3 Criterio de alto ROI

> **No todo criterio de aceptación debe automatizarse desde la interfaz de usuario.**

Este principio guió cada decisión de qué automatizar en qué capa:

**Escenarios automatizados en UI** cuando:
- La validación es inherentemente visual (un elemento está o no en pantalla).
- El flujo conecta múltiples pantallas de forma que la interacción real del usuario importa.
- La regla de negocio se manifiesta como un estado observable en el navegador.

**Escenarios mantenidos en API/integración** cuando:
- La lógica depende de temporizadores o schedulers internos del backend
  (ej. expiración de reserva en 10 minutos, ciclo del `ExpirationService` cada 60 s).
- La validación requiere múltiples actores concurrentes que no tienen representación visual.
- El comportamiento es determinista a nivel de API pero no-reproducible a nivel de UI
  sin introducir `Thread.sleep()` o condiciones de carrera.
- El esfuerzo de setup supera el valor que aporta verlo "desde la ventana del navegador".

**Impacto en la suite Serenity:**
A raíz de este principio, el flujo de *restauración de cupo post pago fallido* fue
deliberadamente excluido de la capa UI. La secuencia `PAYMENT_FAILED → reservation.expiresAt
→ ExpirationService.incrementTierQuota()` tiene una latencia mínima de 10 minutos por
diseño del TTL de reserva, más el ciclo del scheduler. Este flujo se verifica de forma
íntegra, eficiente y sin esperas artificiales en la suite Karate.

---

### 4.4 Matriz de cobertura por historia de usuario

Las HUs corresponden a las **historias de usuario oficiales del proyecto** (`PRD_BACKLOG/USER_STORIES.md`).

| ID    | Historia de usuario oficial                                    | Serenity UI | Karate API | k6 | SQL |
|-------|----------------------------------------------------------------|:-----------:|:----------:|:--:|:---:|
| HU-01 | Creación de evento de obra de teatro (admin)                  |    ⬜       |     ✅     |    |     |
| HU-02 | Configuración de tiers y precios por evento (admin)           |    ⬜       |     ✅     |    |     |
| **HU-03** | **Visualización de eventos y disponibilidad** (comprador) |  **✅**    |     ✅     |    |  ✅ |
| **HU-04** | **Reserva y compra de entrada con pago simulado** (comprador) | **✅**   |     ✅     | ✅ |  ✅ |
| HU-05 | Liberación automática por fallo de pago o expiración          |    ⬜ ¹    |     ✅     |    |  ✅ |
| HU-06 | Notificaciones al comprador                                   |    ⬜       |     ✅     |    |     |
| **HU-07** | **Visualización de ticket confirmado** (comprador)        |  **✅**    |     ✅     |    |  ✅ |
| —     | Concurrencia en compra de última entrada                      |    ⬜ ¹    |     ✅     | ✅ |     |

> ¹ No automatizable en UI sin `Thread.sleep()`: lógica scheduler-driven o multi-actor concurrente.  
> ⬜ = Fuera del alcance de la capa UI por diseño. Cubierto en la capa indicada.  
> HU-01 y HU-02 son operaciones de administración sin flujo UI del comprador. HU-05 y HU-06 son lógica de backend/scheduler sin interacción directa en la pantalla del comprador.

**Cobertura UI ejecutada (Serenity):** HU-03, HU-04 (parcial — flujo guest), HU-07 — 9 escenarios, 0 fallos, 0 omisiones.

---

## 5. Resultados de ejecución

### 5.1 Serenity BDD — Funcional UI

```
mvn clean test

Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

URL base por defecto: `http://localhost:5173`. Ejecución validada contra el frontend Docker.

| # | Feature | Escenario | Tags | Resultado |
|---|---------|-----------|------|-----------|
| 1 | guest_happy_path | Flujo completo de compra exitosa | @smoke | ✅ PASS |
| 2 | guest_happy_path | Outline: tier VIP | — | ✅ PASS |
| 3 | guest_happy_path | Outline: tier GENERAL | — | ✅ PASS |
| 4 | payment_failure | Pago rechazado — pantalla de error y reintento | @smoke | ✅ PASS |
| 5 | ticket_visibility | Ticket confirmado visible tras pago exitoso | @smoke | ✅ PASS |
| 6 | ticket_visibility | Sin ticket confirmado tras pago rechazado | — | ✅ PASS |
| 7 | event_availability | Cartelera muestra al menos un evento disponible | @smoke | ✅ PASS |
| 8 | event_availability | Tier VIP agotado marcado como no disponible | @requires-seed-data | ✅ PASS |
| 9 | event_availability | Early Bird vencido no disponible para selección | @requires-seed-data | ✅ PASS |

### 5.2 Karate DSL — API y lógica de negocio

29 casos ejecutados. Cobertura: contratos REST, estados de reserva, lógica de quota,
expiración, concurrencia, notificaciones, autenticación y administración.

*(Resultados detallados en el repositorio Karate.)*

### 5.3 k6 — Rendimiento

Escenario de carga sobre el ciclo reserva + pago. Métricas objetivo:
latencia p95 ≤ 500 ms, tasa de error ≤ 1 % bajo carga sostenida.

*(Resultados detallados en el repositorio k6.)*

### 5.4 SQL — Consistencia de datos

Consultas de verificación ejecutadas sobre `events_db` / `ticketing_db`
confirmando integridad de quota, estado de tickets y límpieza de reservas expiradas.

---

## 6. Escenarios fuera del alcance de la capa UI

Los siguientes escenarios fueron analizados y excluidos deliberadamente de la
automatización UI por las razones indicadas. Están cubiertos en la capa Karate.

| Escenario | Razón de exclusión de UI |
|-----------|--------------------------|
| Restauración de cupo tras pago fallido | Scheduler-driven. TTL mínimo de 10 min + ciclo de 60 s. Requeriría `Thread.sleep()` → frágil e impráctica. |
| Compra concurrente sobre última entrada | Requiere dos actores simultáneos. No modelable en Serenity sin paralelismo forzado. |
| Limite de reintentos de pago (MaxPaymentAttemptsExceededException) | El límite es N intentos a nivel de API. La UI no expone una interfaz dedicada para forzar ese estado de forma controlada. |
| Notificación push tras pago | Canal WebSocket/RabbitMQ. No verificable de forma confiable desde Selenium sin mocks. |
| Expiración de reserva por timer (PENDING → EXPIRED) | ídem scheduler. La UI solo muestra el estado final; el transition es opaco al navegador. |

---

## 7. Riesgos y mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|:---:|:---:|---|
| Frontend no corre en Docker con botones mock | Baja | Alto | El entorno estándar (Docker) activa botones mock automáticamente. En modo local fuera de Docker: usar `npm run dev:e2e`. Documentado en README. |
| Cambio de selectores DOM en el frontend | Media | Medio | Selectores por `data-testid` y `aria-*` priorizados. Evitar clases CSS generadas por CSS Modules. |
| Datos de seed alterados (V16) | Baja | Alto | La migración Flyway `V16` garantiza reproducibilidad. Verificar con `PGPASSWORD=postgres psql ...`. |
| ChromeDriver desactualizado | Baja | Bajo | WebDriverManager gestiona la versión compatible automáticamente. |
| Scheduler de expiración tardío | N/A | N/A | Este riesgo no aplica a la capa UI por diseño deliberado (ver sección 4.3). |
