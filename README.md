# Ticketing MVP — Automatización Funcional UI (Serenity BDD)

> Automatización funcional E2E de la UI del comprador para el proyecto **Ticketing MVP** (venta de entradas de teatro).  
> Stack: Java · Serenity BDD · Cucumber · Screenplay Pattern · Maven · Chrome

---

## Índice

1. [Propósito](#propósito)
2. [Posición en la estrategia de calidad](#posición-en-la-estrategia-de-calidad)
3. [Stack técnico](#stack-técnico)
4. [Prerrequisitos](#prerrequisitos)
5. [Cómo levantar el frontend](#cómo-levantar-el-frontend)
6. [Estructura del proyecto](#estructura-del-proyecto)
7. [Ejecución de la suite](#ejecución-de-la-suite)
8. [Parametrización de URL base](#parametrización-de-url-base)
9. [Cobertura actual](#cobertura-actual)
10. [Diseño de la automatización](#diseño-de-la-automatización)
11. [Reportes Serenity](#reportes-serenity)
12. [Limitaciones actuales y próximos pasos](#limitaciones-actuales-y-próximos-pasos)
13. [Qué no debe incluirse en el ZIP de entrega](#qué-no-debe-incluirse-en-el-zip-de-entrega)

---

## Propósito

Este repositorio implementa la **automatización funcional de la interfaz de usuario** del Ticketing MVP.  
Los tests validan los flujos del comprador anónimo (guest) desde el navegador, ejerciendo la UI de extremo a extremo:

```
cartelera → selección de evento → selección de tier → checkout → pago simulado → confirmación o rechazo
```

El repositorio **no reemplaza** ni duplica otras capas de calidad del proyecto; es un componente independiente dentro de una estrategia multicapa.

> **Alcance de capa:** Serenity cubre la **capa UI funcional priorizada del comprador** — lo que solo es observable y verificable desde el navegador. La validación exhaustiva de API, reglas de negocio internas y lógica de backend corresponde a **Karate**. Las pruebas de rendimiento bajo carga concurrente corresponden a **k6**.

---

## Posición en la estrategia de calidad

| Capa                                               | Herramienta        | Repositorio   |
|----------------------------------------------------|--------------------|---------------|
| Funcional UI — flujos prioritarios del comprador   | **Serenity BDD**   | *este repo*   |
| API / Contrato / Lógica de negocio backend (validación exhaustiva) | Karate DSL | repo separado |
| Rendimiento bajo carga concurrente                 | k6                 | repo separado |
| Consistencia de datos                              | SQL (evidencia)    | repo separado |

> **Principio de diseño:** no todo criterio de aceptación debe automatizarse por UI. Los flujos con lógica de scheduler, temporizadores o estados internos del backend (expiración de reserva, concurrencia multi-actor, notificaciones RabbitMQ) se validan con mayor estabilidad y menor costo en la capa API/integración (Karate). Este repositorio cubre los recorridos de usuario que **solo son visibles y verificables desde el navegador**.

---

## Stack técnico

| Componente       | Versión / Detalle                              |
|------------------|------------------------------------------------|
| Java             | 8+ (validado con 11 y 17)                      |
| Serenity BDD     | 5.3.8                                          |
| Cucumber JVM     | 7.34.2                                         |
| JUnit Platform   | 6.0.3                                          |
| Maven            | 3.8+                                           |
| Build tool       | **Maven exclusivamente** (sin Gradle)          |
| Navegador        | Google Chrome (driver gestionado automáticamente por WebDriverManager) |
| Patrón de diseño | Screenplay                                     |
| Selector base    | XPath por texto/rol + `data-testid` donde disponible |

---

## Prerrequisitos

1. **Java 8 o superior** con `JAVA_HOME` configurado.
2. **Maven 3.8+** (`mvn -v` para verificar).
3. **Google Chrome** instalado — WebDriverManager descarga el ChromeDriver compatible de forma automática.
4. El **frontend del Ticketing MVP** disponible y corriendo en `http://localhost:5173` (ver sección siguiente).

---

## Cómo levantar el frontend

### Entorno estándar (Docker — configuración oficial del proyecto)

El entorno estándar del proyecto levanta el frontend mediante Docker Compose. En esta configuración el frontend ya corre en `http://localhost:5173` con los botones de pago simulado (`Simular Pago Exitoso` / `Simular Pago Rechazado`) activados.

```bash
# Desde la raíz del repositorio principal del proyecto
docker compose up -d
```

La suite ya fue ejecutada exitosamente contra el frontend en esta URL. **No se requiere ningún paso adicional para el flujo estándar.**

### Entorno alternativo (desarrollo local fuera de Docker)

Si el frontend no corre en Docker sino directamente con Node.js, debe iniciarse en **modo E2E** para que los botones de pago simulado queden disponibles:

```bash
# Desde el repositorio del frontend (alternativa fuera de Docker)
npm run dev:e2e
```

> **Nota:** `npm run dev` estándar no expone los botones mock de pago. Usar `npm run dev:e2e` **solo** si el frontend no está corriendo via Docker. En cualquier caso, el puerto de destino es `http://localhost:5173`.

---

## Estructura del proyecto

```
src/
└── test/
    ├── java/
    │   └── ticketing/
    │       ├── CucumberTestSuite.java              ← Runner (JUnit Platform Suite)
    │       ├── navigation/
    │       │   └── NavigateTo.java                 ← Abre /eventos en el navegador
    │       ├── screenplay/
    │       │   ├── tasks/
    │       │   │   ├── SelectEvent.java             ← Clic en tarjeta de evento
    │       │   │   ├── SelectTier.java              ← Clic en tier (VIP/GENERAL/EARLY_BIRD)
    │       │   │   ├── ReserveTicket.java           ← Clic en "Reservar"
    │       │   │   ├── EnterEmail.java              ← Escribe email del invitado
    │       │   │   ├── ContinueToPayment.java       ← Clic en "Continuar al Pago"
    │       │   │   └── CompletePayment.java         ← Pago exitoso o rechazado (mock)
    │       │   ├── questions/
    │       │   │   ├── SuccessScreen.java           ← Verifica pantalla de confirmación
    │       │   │   ├── TicketScreen.java            ← Presencia/ausencia del ticket confirmado
    │       │   │   └── EventsListScreen.java        ← Conteo de eventos en cartelera
    │       │   └── ui/
    │       │       ├── EventsPage.java              ← [data-testid^="event-card-"]
    │       │       ├── EventDetailPage.java         ← XPath por tier (General/VIP/Early Bird)
    │       │       ├── CheckoutPage.java            ← #checkout-email, XPath continuar
    │       │       ├── PaymentPage.java             ← XPath botones mock pago
    │       │       ├── ConfirmationPage.java        ← XPath "¡Pago aprobado!"
    │       │       └── FailedPaymentPage.java       ← XPath "Pago declinado." + reintentar
    │       └── stepdefinitions/
    │           ├── ParameterDefinitions.java        ← Tipo {actor} + @Before stage
    │           └── GuestPurchaseStepDefinitions.java← Glue code de todos los features
    └── resources/
        ├── features/ticketing/
        │   ├── guest_happy_path.feature            ← 3 escenarios (1 smoke + 2 outline)
        │   ├── payment_failure.feature             ← 1 escenario (smoke)
        │   ├── ticket_visibility.feature           ← 2 escenarios
        │   └── event_availability.feature          ← 3 escenarios (1 smoke + 2 seed-data)
        ├── serenity.conf                           ← WebDriver + entornos
        ├── junit-platform.properties               ← Paralelismo y filtros
        └── logback-test.xml                        ← Logging
```

---

## Ejecución de la suite

La URL base (`http://localhost:5173`) está configurada como valor por defecto en `serenity.conf` y en `pom.xml`.  
**No es necesario pasar ningún parámetro adicional para el flujo local estándar contra Docker.**

### Ejecución estándar (comando oficial)

```bash
mvn clean test
```

Resultado esperado:

```
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

> El reporte HTML Serenity se genera automáticamente al finalizar en `target/site/serenity/index.html`.

### Solo escenarios @smoke

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

### Solo una feature específica

```bash
# Guest happy path (flujo de compra exitosa)
mvn clean test -Dcucumber.filter.tags="@guest-happy-path"

# Pago rechazado
mvn clean test -Dcucumber.filter.tags="@payment-failure"

# Visibilidad de ticket confirmado
mvn clean test -Dcucumber.filter.tags="@ticket-visibility"

# Disponibilidad en cartelera
mvn clean test -Dcucumber.filter.tags="@event-availability"
```

### Headless (para CI/CD)

```bash
mvn clean test -Dheadless.mode=true
```

### Solo compilar sin ejecutar

```bash
mvn test-compile -q
```

---

## Parametrización de URL base

La URL por defecto es `http://localhost:5173` y está declarada en `src/test/resources/serenity.conf` bajo el entorno `default`.  
Para entornos distintos al local estándar, usa `-Denvironment` o el override `-Dwebdriver.base.url`:

```bash
# Staging
mvn clean test -Denvironment=staging

# Override puntual a cualquier URL
mvn clean test -Dwebdriver.base.url=https://otro-host.example.com
```

Entornos configurados en `src/test/resources/serenity.conf`:

```hocon
environments {
  default { webdriver.base.url = "http://localhost:5173" }
  dev     { webdriver.base.url = "http://localhost:5173" }
  staging { webdriver.base.url = "https://staging.ticketing.example.com" }
  prod    { webdriver.base.url = "https://ticketing.example.com" }
}
```

`-Dwebdriver.base.url` tiene precedencia sobre el valor en `serenity.conf`.

---

## Cobertura actual

### Estado de ejecución

```
Tests run: 9  |  Failures: 0  |  Errors: 0  |  Skipped: 0
BUILD SUCCESS
```

Ejecución validada contra el frontend Docker en `http://localhost:5173`.

### Escenarios por feature

#### `guest_happy_path.feature` — `@guest-happy-path @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 1 | Invitado completa el flujo de compra exitosamente de principio a fin | `@smoke` |
| 2 | Invitado elige tier VIP y completa la compra | outline |
| 3 | Invitado elige tier GENERAL y completa la compra | outline |

Flujo cubierto: `/eventos` → tarjeta de evento → selección de tier → Reservar → checkout (email) → Continuar al Pago → Simular Pago Exitoso → pantalla de confirmación.

> Nota: el tier EARLY_BIRD de *The Phantom's Echo* está expirado por diseño (condición fijada en V16 de migraciones). El outline cubre VIP y GENERAL — ambos activos.

#### `payment_failure.feature` — `@payment-failure @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 4 | Invitado intenta pagar pero el pago es rechazado y ve la pantalla de fallo con opción de reintento | `@smoke` |

Valida: mensaje "Pago declinado.", presencia del botón "Reintentar Pago".

#### `ticket_visibility.feature` — `@ticket-visibility @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 5 | Invitado visualiza el ticket confirmado tras una compra exitosa | `@smoke` |
| 6 | Invitado con pago rechazado no visualiza el ticket confirmado | — |

Valida: texto "¡Pago aprobado!" visible (escenario positivo); ausencia de ese elemento en pantalla de fallo (escenario negativo).

#### `event_availability.feature` — `@event-availability @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 7 | La cartelera muestra al menos un evento disponible | `@smoke` |
| 8 | Comprador observa un tier agotado como no disponible en pantalla | `@requires-seed-data` |
| 9 | Comprador no ve el Early Bird como opción de compra activa cuando su período venció | `@requires-seed-data` |

Escenario 7: Valida existencia de al menos una tarjeta `[data-testid^="event-card-"]` en `/eventos`.

Escenarios 8-9: Validan disponibilidad de tiers. Precondiciones persistidas en `V16__create_test_scenario_conditions.sql`:
- BODAS DE SANGRE · VIP: `quota = 0` → `aria-disabled="true"` en el tier card
- The Phantom's Echo · EARLY_BIRD: `valid_until` en el pasado → mismo estado

### Correspondencia con HUs oficiales del proyecto

Esta suite cubre la **capa UI funcional priorizada del comprador**. Las HUs cubiertas corresponden a las historias de usuario oficiales del proyecto (`PRD_BACKLOG/USER_STORIES.md`):

| HU oficial | Descripción oficial | Cobertura en Serenity UI | Capa complementaria |
|------------|---------------------|--------------------------|--------------------|
| **HU-03** | Visualización de eventos y disponibilidad | ✅ Escenarios 7, 8, 9 — cartelera, tier agotado, Early Bird vencido | Karate (API) |
| **HU-04** | Reserva y compra de entrada con pago simulado | ✅ Escenarios 1–6 — flujo completo guest, pago exitoso y rechazado | Karate (API) |
| **HU-07** | Visualización de ticket confirmado | ✅ Escenarios 5, 6 — ticket visible tras pago exitoso; ausente tras rechazo | Karate (API) |

> **Nota importante:** HU-01 (Creación de evento), HU-02 (Configuración de tiers), HU-05 (Liberación automática) y HU-06 (Notificaciones) son funcionalidades backend/admin que no tienen representación directa en flujos UI del comprador anónimo. Están cubiertas íntegramente por **Karate**. No se redefinen ni renumeran en este repositorio.

---

## Diseño de la automatización

### Patrón Screenplay

Toda la lógica de interacción está organizada siguiendo el **Screenplay Pattern**:

- **Tasks**: acciones de alto nivel que un actor realiza (`SelectEvent`, `CompletePayment`, etc.).
- **Questions**: inspecciones del estado de la UI (`SuccessScreen.isVisible()`, `TicketScreen.isAbsent()`, `EventsListScreen.hasAtLeastOneEvent()`).
- **UI Targets**: locators centralizados por pantalla (`EventsPage`, `EventDetailPage`, `ConfirmationPage`, etc.).

### Selectores

Los selectores se establecen por auditoría directa del DOM del frontend en cada iteración. La estrategia aplicada:

- `[data-testid^="event-card-"]` donde el frontend expone `data-testid`.
- XPath por texto visible normalizado (`normalize-space()`) para elementos sin `data-testid`.
- Sin selectores por clase CSS ni por posición relativa frágil.

### Esperas

No se usa `Thread.sleep()` en ningún archivo del proyecto. Todas las esperas son explícitas mediante `WaitUntil.the(target, matcher).forNoMoreThan(N).seconds()`, ejecutadas dentro de cada tarea o step antes de la acción o assertion correspondiente.

### Unicidad de email

El task `EnterEmail` genera un sufijo de timestamp (`yyyyMMddHHmmss`) antes del `@` para garantizar unicidad de email entre ejecuciones sin necesidad de limpiar datos del backend.

---

## Reportes Serenity

### Generación

El reporte HTML se genera automáticamente con cada ejecución de `mvn clean test`. Para regenerarlo sobre resultados existentes:

```bash
mvn serenity:aggregate
```

### Ubicación

```
target/site/serenity/index.html
```

### Contenido del reporte

- Resumen de resultados por feature y escenario.
- Línea de tiempo de ejecución (`target/build/test-results/timeline/index.html`).
- Capturas de pantalla automáticas en caso de fallo (`serenity.take.screenshots = FOR_FAILURES`).
- Narración de cada paso tal como la describe el Screenplay (ej. `Marta clicks on botón simular pago exitoso`).
- Contexto de ejecución: navegador, SO, actor, duración por paso.

---

## Limitaciones actuales y próximos pasos

### Limitaciones conocidas

| Limitación | Detalle |
|---|---|
| Sin flujo autenticado | El repositorio cubre únicamente el flujo de compra como visitante anónimo (guest). Los flujos de login, historial y admin están en Karate. |
| Sin validaciones de formulario | No se cubren mensajes de error por email inválido ni campos vacíos. |
| Sin tests responsive/móvil | La suite corre en Chrome escritorio 1280×900. |
| Pago simulado requiere modo E2E | Los tests de pago requieren que el frontend tenga los botones mock disponibles. En Docker esto es automático. En modo local: usar `npm run dev:e2e`. |
| Expiración y restauración de cupo | Este flujo es scheduler-driven (TTL 10 min + ciclo de 60 s). No es automatizable por UI sin sleeps. Cubierto en Karate. |

### Próximos pasos recomendados

1. **Validaciones de formulario de checkout**
   - Email inválido → mensaje de error visible.
   - Campo vacío → botón "Continuar al Pago" deshabilitado.

2. **CI/CD**
   - El workflow `.github/workflows/maven.yml` ya existe. Ampliar con `mvn clean test -Dheadless.mode=true` y publicar el reporte Serenity como artefacto del pipeline.

3. **Datos de prueba**
   - Introducir un `TestDataFactory` si la variedad de escenarios lo amerita.
   - Considerar reset de datos vía API antes de escenarios que requieran estado limpio.

---

## Qué no debe incluirse en el ZIP de entrega

Al empaquetar el repositorio para entrega, excluir obligatoriamente:

| Carpeta / Archivo | Razón |
|---|---|
| `.git/` | Metadatos internos de control de versiones, no pertenecen al entregable |
| `target/` | Artefactos generados por Maven en tiempo de compilación/prueba |
| `.idea/` | Configuración local del IDE (IntelliJ IDEA) |
| `*.iml` | Módulos de proyecto IntelliJ, específicos del entorno local |
| `.DS_Store` | Metadatos del sistema de archivos macOS |
| `build/` | Directorio de output alternativo generado por algunas herramientas |

El ZIP debe contener únicamente: `pom.xml`, `serenity.properties`, `src/`, `.gitignore`, `README.md`, `TEST_PLAN.md`, `.github/`.

---

## Comandos de referencia rápida

```bash
# Ejecución estándar (comando oficial)
mvn clean test

# Solo @smoke
mvn clean test -Dcucumber.filter.tags="@smoke"

# Headless (CI/CD)
mvn clean test -Dheadless.mode=true

# Compilar sin ejecutar
mvn test-compile -q

# Regenerar reporte sobre resultados existentes
mvn serenity:aggregate

# Limpiar resultados anteriores
mvn clean
```

---

## Referencias

- [Serenity BDD — Documentación oficial](https://serenity-bdd.github.io/theserenitybook/latest/)
- [Screenplay Pattern Guide](https://serenity-bdd.github.io/theserenitybook/latest/screenplay.html)
- [serenity-cucumber-starter (template base)](https://github.com/serenity-bdd/serenity-cucumber-starter)
- [Cucumber JVM — Documentación](https://cucumber.io/docs/cucumber/)
- [JUnit Platform — Cucumber integration](https://cucumber.io/docs/cucumber/api/?lang=java#junit-platform)
- [Historias de Usuario oficiales del proyecto](../PRD_BACKLOG/USER_STORIES.md)
