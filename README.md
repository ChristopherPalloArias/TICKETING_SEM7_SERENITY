<div align="center">

# 🚀 TICKETING_MVP_UI_TEST

### Taller Semana 7: Expectativa vs. Realidad — Ejecución Ágil, MVP y Estrategia de Pruebas

**Rol / Líder QA:** Christopher Ismael Pallo Arias  
**Proyecto:** Construcción del Ticketing MVP real y su Certificación por Micro-Sprints (Fase UI — SerenityBDD)  
**Objetivo:** Vivir "el choque con la realidad" y certificar como QA el MVP funcional construido por DEV. Automatizar los flujos del comprador desde el navegador real usando el **Screenplay Pattern** de SerenityBDD + Cucumber, garantizando que la experiencia de compra sea correcta, estable y verificable de forma reproducible.

<br />

### 🛠️ Technology Stack

**Functional UI Testing Framework**
<br />
<img src="https://img.shields.io/badge/Serenity_BDD-5.3.8-009CDE?style=for-the-badge&logo=java&logoColor=white" alt="Serenity BDD" />
<img src="https://img.shields.io/badge/Cucumber-7.34.2-23D96C?style=for-the-badge&logo=cucumber&logoColor=white" alt="Cucumber JVM" />
<img src="https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
<img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
<img src="https://img.shields.io/badge/Chrome-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white" alt="Chrome" />
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
<br />
<a href="https://skillicons.dev">
  <img src="https://skillicons.dev/icons?i=java,github,docker" alt="Automation Stack" />
</a>

</div>

---

## 📌 Panel de Entrega y Resultados Formales

> ⚠️ **ATENCIÓN EVALUADOR:** Todos los insumos obligatorios exigidos sobre la validación funcional UI del comprador, el Screenplay Pattern y la estrategia multicapa se encuentran consolidados y listos para su auditoría.
>
> 🔗 **[👉 HAZ CLIC AQUÍ PARA VER EL INFORME OFICIAL DE SERENITY BDD](https://christopherpalloarias.github.io/TICKETING_SEM7_SERENITY/)**
- 📄 **Test Plan Oficial:** [`TEST_PLAN.md`](https://github.com/ChristopherPalloArias/PRD_BACKLOG/blob/main/TEST_PLAN.md) *(Estrategia multicapa documentada: Serenity + Karate + k6 + SQL).*
- 📋 **Matriz de Test Cases:** [`TEST_CASES.md`](https://github.com/ChristopherPalloArias/PRD_BACKLOG/blob/main/TEST_CASES.md) *(29 casos del ciclo MVP con trazabilidad completa.)*
- 📊 **Evidencia de ejecución cruda:** [`serenity_evidence.txt`](./serenity_evidence.txt) *(Log completo de la última ejecución: 9 tests, 0 fallos, BUILD SUCCESS.)*
- 🚀 **Feature files:** Carpeta [`src/test/resources/features/ticketing/`](./src/test/resources/features/ticketing/)

---

## 📋 Tabla de Contenidos

1. [Contexto del Proyecto: El Choque con la Realidad](#-contexto-del-proyecto-el-choque-con-la-realidad)
2. [Resultados de Ejecución](#-resultados-de-ejecución)
3. [Arquitectura y Estructura del Framework](#️-arquitectura-y-estructura-del-framework)
4. [Instrucciones de Clonado y Setup de Backend](#-instrucciones-de-clonado-y-setup-de-backend)
5. [Ejecución de las Pruebas](#️-ejecución-de-las-pruebas)
6. [Datos de Prueba y Seed Data](#-datos-de-prueba-y-seed-data)
7. [Consideraciones Técnicas y Retos Avanzados Resueltos](#-consideraciones-técnicas-y-retos-avanzados-resueltos)

---

## 🎯 Contexto del Proyecto: El Choque con la Realidad

Este repositorio corresponde a la certificación funcional UI (**SerenityBDD + Cucumber**) dentro de la **Fase 3: Estrategia de Calidad** exigida para el Taller 7. Tras diseñar la utopía en la Semana 6, nuestro objetivo de equipo fue construir y testear las piezas críticas seleccionadas del Backlog para entregar un **MVP funcional y valioso**.

Mientras DEV implementaba y lidiaba con la curva real de los Story Points mediante *micro-sprints* iterativos de 2 días, desde el rol de QA se redactó e impuso la arquitectura formal de calidad alojada en el repositorio de Producto (`PRD_BACKLOG`): [`TEST_PLAN.md`](https://github.com/ChristopherPalloArias/PRD_BACKLOG/blob/main/TEST_PLAN.md) y [`TEST_CASES.md`](https://github.com/ChristopherPalloArias/PRD_BACKLOG/blob/main/TEST_CASES.md).

Esta suite ejerce la validación funcional de los flujos del **comprador anónimo (guest)** que son observables y verificables desde el navegador real. Se priorizó la cobertura de los escenarios de mayor valor para el usuario final:

- **HU-03 — Visualización de eventos y disponibilidad:** cartelera con eventos activos, tier agotado marcado visualmente como no disponible, Early Bird vencido oculto o deshabilitado.
- **HU-04 — Reserva y compra con pago simulado:** flujo completo del comprador desde cartelera hasta confirmación, incluyendo los caminos de pago exitoso y pago rechazado.
- **HU-07 — Ticket confirmado:** presencia del ticket tras compra exitosa y su ausencia tras pago rechazado.

---

## ✅ Resultados de Ejecución

```
mvn clean test

Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

**Entorno ejecutado contra:** Frontend Docker en `http://localhost:5173`

| # | Feature | Escenario | Tags | Resultado |
|---|---------|-----------|------|-----------|
| 1 | `guest_happy_path` | Flujo completo de compra exitosa (primera llamada) | `@smoke` | ✅ PASS |
| 2 | `guest_happy_path` | Outline: compra exitosa — tier VIP | `@mvp` | ✅ PASS |
| 3 | `guest_happy_path` | Outline: compra exitosa — tier GENERAL | `@mvp` | ✅ PASS |
| 4 | `payment_failure` | Pago rechazado — pantalla de error y botón de reintento | `@smoke` | ✅ PASS |
| 5 | `ticket_visibility` | Ticket confirmado visible tras pago exitoso | `@smoke` | ✅ PASS |
| 6 | `ticket_visibility` | Sin ticket confirmado tras pago rechazado | `@mvp` | ✅ PASS |
| 7 | `event_availability` | Cartelera muestra al menos un evento disponible | `@smoke` | ✅ PASS |
| 8 | `event_availability` | Tier VIP agotado marcado como no disponible | `@requires-seed-data` | ✅ PASS |
| 9 | `event_availability` | Early Bird vencido no disponible para selección | `@requires-seed-data` | ✅ PASS |

**Cobertura HUs:** HU-03 (escenarios 7–9) · HU-04 (escenarios 1–6) · HU-07 (escenarios 5–6)

---

## 🏗️ Arquitectura y Estructura del Framework

La suite implementa el **Screenplay Pattern** — el patrón de diseño de alto nivel recomendado por SerenityBDD que modela los actores, sus tareas y las preguntas que hacen sobre el sistema. La separación de responsabilidades es estricta:

```
src/test/
├── java/ticketing/
│   ├── CucumberTestSuite.java          ← Runner JUnit Platform Suite
│   ├── navigation/
│   │   └── NavigateTo.java             ← Tareas de navegación (Open.url)
│   ├── screenplay/
│   │   ├── tasks/                      ← Lo que el actor HACE
│   │   │   ├── SelectEvent.java        ← Seleccionar evento en cartelera
│   │   │   ├── SelectTier.java         ← Elegir tier (VIP / GENERAL / EARLY_BIRD)
│   │   │   ├── ReserveTicket.java      ← Confirmar reserva
│   │   │   ├── EnterEmail.java         ← Ingresar correo en checkout
│   │   │   ├── ContinueToPayment.java  ← Avanzar al paso de pago
│   │   │   └── CompletePayment.java    ← Pago exitoso o rechazado (botones mock)
│   │   ├── questions/                  ← Lo que el actor PREGUNTA sobre el sistema
│   │   │   ├── EventsListScreen.java   ← ¿Hay eventos en cartelera?
│   │   │   ├── SuccessScreen.java      ← ¿Se ve la pantalla de confirmación?
│   │   │   ├── TicketScreen.java       ← ¿El ticket está o está ausente?
│   │   │   └── TierAvailabilityScreen.java ← ¿El tier está deshabilitado o ausente?
│   │   └── ui/                         ← DÓNDE están los elementos (Targets)
│   │       ├── EventsPage.java         ← Locators de la cartelera (/eventos)
│   │       ├── EventDetailPage.java    ← Locators de detalle y tiers
│   │       ├── CheckoutPage.java       ← Locators de checkout (email, continuar)
│   │       ├── PaymentPage.java        ← Locators de botones de pago mock
│   │       ├── ConfirmationPage.java   ← Locators de pantalla de éxito
│   │       └── FailedPaymentPage.java  ← Locators de pantalla de rechazo
│   └── stepdefinitions/
│       ├── GuestPurchaseStepDefinitions.java  ← Glue Gherkin → Screenplay
│       └── ParameterDefinitions.java          ← Tipo {actor} + @Before stage
└── resources/
    ├── features/ticketing/             ← Feature files BDD
    │   ├── guest_happy_path.feature    ← HU-03 + HU-04 (flujo completo guest)
    │   ├── payment_failure.feature     ← HU-04 CA-02 (pago rechazado)
    │   ├── ticket_visibility.feature   ← HU-07 (presencia/ausencia de ticket)
    │   └── event_availability.feature  ← HU-03 (cartelera, tiers agotados/vencidos)
    ├── serenity.conf                   ← Configuración de entornos y Chrome
    ├── junit-platform.properties       ← Paralelismo desactivado, filtros de tag
    └── logback-test.xml                ← Configuración de logging
```

### Capas del Screenplay Pattern

| Capa | Responsabilidad | Ejemplo |
|---|---|---|
| **Tasks** | Lo que el actor *hace* — acciones sobre la UI | `SelectTier.called("GENERAL")` |
| **Questions** | Lo que el actor *pregunta* sobre el DOM | `TicketScreen.isConfirmed()` |
| **UI Targets** | *Dónde* están los elementos en el DOM | `EventDetailPage.tierButton("VIP")` |
| **Step Definitions** | *Glue* — orquesta Tasks y Questions | `actor.attemptsTo(...)` + `Ensure.that(...)` |

---

## ⚡ Instrucciones de Clonado y Setup de Backend

> ⚠️ **Crítico:** Las pruebas UI requieren que el frontend y todos los microservicios estén corriendo. El entorno estándar es Docker Compose.

### 1. Clonar y Levantar el Clúster Backend + Frontend

```bash
# Clonar el ecosistema completo del backend
git clone https://github.com/ChristopherPalloArias/TICKETING_SEM7.git
cd TICKETING_SEM7

# Configurar variables de entorno
cp .env.template .env

# Levantar la topología completa (backend + frontend + bases de datos)
docker-compose up -d --build
```

Verificar que los servicios responden:

| Servicio | Puerto | Verificación |
|---|---|---|
| Frontend (Vite + React) | `localhost:5173` | Abrir en Chrome — debe mostrar cartelera |
| API Gateway | `localhost:8080` | `curl http://localhost:8080/api/v1/events` |
| ms-events | `localhost:8081` | `curl http://localhost:8081/actuator/health` |
| ms-ticketing | `localhost:8082` | `curl http://localhost:8082/actuator/health` |
| ms-notifications | `localhost:8083` | `curl http://localhost:8083/actuator/health` |

> 💡 **Entorno alternativo (sin Docker):** Si el frontend ya corre con `npm run dev:e2e` en el repo del frontend, las pruebas también funcionan. Este modo activa los botones de pago mock sin necesidad de Docker. La URL base sigue siendo `http://localhost:5173`.

---

### 2. Preparar el Entorno de Automatización SerenityBDD

```bash
# Clonar este repositorio
git clone https://github.com/ChristopherPalloArias/TICKETING_SEM7_SERENITY.git
cd TICKETING_SEM7_SERENITY

# Java 17 requerido — verificar versión
java -version

# Descargar dependencias Maven (sin ejecutar tests)
mvn clean install -DskipTests
```

**WebDriverManager** gestiona automáticamente la versión de ChromeDriver compatible. No es necesario instalar ni configurar ChromeDriver manualmente.

---

## ▶️ Ejecución de las Pruebas

### Ejecución completa (comando oficial)

```bash
mvn clean test
```

El reporte de Serenity se genera en `target/site/serenity/index.html`.

---

### Filtrar por tag desde CLI

```bash
# Solo escenarios de humo (@smoke) — los más críticos, sub-minuto
mvn clean test -Dcucumber.filter.tags="@smoke"

# Solo escenarios que requieren seed data con condiciones especiales
mvn clean test -Dcucumber.filter.tags="@requires-seed-data"

# Solo el flujo del guest happy path completo
mvn clean test -Dcucumber.filter.tags="@guest-happy-path"

# Solo escenarios de pago rechazado
mvn clean test -Dcucumber.filter.tags="@payment-failure"
```

---

### Cambiar entorno de ejecución

```bash
# Entorno estándar (default) — Docker en localhost:5173
mvn clean test

# Especificar URL directamente
mvn clean test -Dwebdriver.base.url=http://localhost:5173

# Entorno alternativo de desarrollo local
mvn clean test -Dwebdriver.base.url=http://localhost:3001

# Entorno staging
mvn clean test -Denvironment=staging
```

---

### Ejecutar en modo headless (para CI/CD)

```bash
mvn clean test -Dheadless.mode=true
```

---

### Guardar log de ejecución como evidencia

```bash
mvn clean test 2>&1 | tee serenity_execution_$(date +%Y%m%d_%H%M%S).txt
```

---

## 🌱 Datos de Prueba y Seed Data

La suite depende de migraciones Flyway aplicadas automáticamente al levantar `ms-events`:

| Migración | Propósito |
|---|---|
| `V7__seed_demo_data.sql` | 4 eventos `PUBLISHED` con sus 9 tiers configurados y con cupos disponibles. Garantiza que la cartelera muestre eventos y que el flujo de compra sea completable. |
| `V16__create_test_scenario_conditions.sql` | Condiciones especiales para HU-03: **BODAS DE SANGRE · VIP** con `quota = 0` (tier AGOTADO) y **The Phantom's Echo · EARLY_BIRD** con `valid_until` en el pasado (tier VENCIDO). Requerido para los escenarios `@requires-seed-data`. |

> ⚠️ **Si los escenarios `@requires-seed-data` fallan**, verificar que la migración V16 fue aplicada:
> ```bash
> PGPASSWORD=postgres psql -h localhost -p 5433 -U postgres -d events_db \
>   -c "SELECT tier_type, quota, valid_until FROM tiers WHERE event_id IN \
>       (SELECT id FROM events WHERE title ILIKE '%Bodas%' OR title ILIKE '%Phantom%');"
> ```

---

## 🧩 Consideraciones Técnicas y Retos Avanzados Resueltos

### Screenplay Pattern — Separación estricta de responsabilidades

A diferencia del patrón Page Object Model tradicional, el **Screenplay Pattern** organiza la automatización en torno a los *actores* del sistema (en este caso, el comprador anónimo Marta). Cada actor puede intentar *tareas* y hacer *preguntas* sobre el estado del sistema.

```java
// Patrón correcto — actor orquesta tareas y preguntas:
actor.attemptsTo(
    SelectTier.called("GENERAL"),
    ReserveTicket.now(),
    EnterEmail.withAddress("marta.guest@example.com")
);
actor.attemptsTo(
    Ensure.that(TicketScreen.isConfirmed()).isTrue()
);
```

Esto garantiza que los step definitions no contengan lógica de interacción directa con el DOM — solo orquestación de alto nivel, lo que hace la suite **legible, mantenible y sin acoplamiento a implementaciones de WebDriver**.

---

### Esperas explícitas sin `Thread.sleep()`

Toda la suite elimina completamente las esperas estáticas con `Thread.sleep()`. Las esperas son explícitas y orientadas al estado real del DOM:

```java
// WaitUntil aplicado antes de toda interacción:
WaitUntil.the(EventDetailPage.tierButton("GENERAL"), isClickable())
         .forNoMoreThan(10).seconds()
```

Este patrón se aplica consistentemente en cada Task y en los step definitions de validación, respetando el tiempo real de respuesta del frontend sin introducir fragilidd.

---

### Selectores estables: `data-testid` y `aria-*` sobre clases CSS

Los selectores se diseñaron en coordinación con DEV para resistir refactorizaciones del frontend:

| Estrategia | Ejemplo | Uso |
|---|---|---|
| `data-testid` | `[data-testid^='event-card-']` | Tarjetas de evento en cartelera |
| `aria-disabled` | `//div[@role='button' and @aria-disabled='true']` | Tiers no disponibles |
| XPath `normalize-space()` | `//button[.//span[normalize-space()='Reservar']]` | Botones sin `data-testid` |
| XPath `contains()` | `//button[contains(normalize-space(.), 'Reintentar Pago')]` | Texto variable (Nº de intento) |
| `concat()` en XPath | Apóstrofes en títulos: *"The Phantom's Echo"* | Manejo seguro de caracteres especiales |

**Se evitan completamente** las clases CSS generadas por CSS Modules (`_class_hashrandom`) ya que cambian en cada build de producción.

---

### Escenarios excluidos de UI por diseño y ROI

> **No todo criterio de aceptación conviene automatizarse desde el navegador.**

Los siguientes flujos se analizaron y **excluyen deliberadamente de Serenity**, cubiertos íntegramente por Karate:

| Escenario | Razón de exclusión de UI |
|---|---|
| Expiración de reserva (PENDING → EXPIRED) | Scheduler-driven. TTL mínimo de 10 min + ciclo de `ExpirationService` cada 60 s. Requeriría `Thread.sleep(600000)` → frágil e inviable. |
| Liberación automática de cupo | Consecuencia del scheduler. La UI solo muestra el estado final; la transición es opaca al navegador. |
| Compra concurrente sobre última entrada | Requiere dos actores simultáneos. No modelable en Serenity sin paralelismo forzado de ChromeDriver. |
| Notificaciones RabbitMQ | Canal WebSocket/RabbitMQ. No verificable desde Selenium sin mocks externos. |
| Límite de reintentos de pago | Estado interno del backend. La UI no expone un indicador de "intento N de N" verificable de forma determinista. |

Esta decisión **reduce fragilidad, elimina esperas artificiales y mejora la mantenibilidad** de la suite UI. La cobertura de estos flujos en Karate es exhaustiva y sin tiempos de espera gracias a la `Time Travel API` del backend.

---

### Email con timestamp — idempotencia entre corridas

El paso de ingreso de email genera automáticamente una dirección única por corrida:

```java
// marta.guest@example.com → marta.guest+20260408163052@example.com
private static String toUniqueEmail(String email) {
    String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    int at = email.indexOf('@');
    return email.substring(0, at) + "+" + ts + email.substring(at);
}
```

Esto garantiza que cada escenario opera con un email fresco, evitando colisiones en la base de datos entre corridas consecutivas sin necesidad de scripts de limpieza.

---

### Pregunta defensiva `TicketScreen.isAbsent()`

Para el escenario negativo (sin ticket tras pago rechazado), la `Question` implementa un patrón defensivo con `try-catch`:

```java
public static Question<Boolean> isAbsent() {
    return actor -> {
        try {
            return !ConfirmationPage.SUCCESS_TITLE
                    .resolveFor(actor)
                    .isDisplayed();
        } catch (Exception e) {
            // Elemento ausente del DOM → ticket ausente → correcto
            return true;
        }
    };
}
```

Esto funciona correctamente tanto si el elemento no existe en el DOM (caso esperado tras pago rechazado) como si existe pero no es visible.

---

