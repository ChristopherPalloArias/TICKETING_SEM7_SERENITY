# Ticketing MVP — Automatización Funcional UI

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

---

## Propósito

Este repositorio implementa la **automatización funcional de la interfaz de usuario** del Ticketing MVP.
Los tests validan los flujos de compra del visitante anónimo (guest) desde el navegador, ejerciendo la UI de extremo a extremo: cartelera → selección de evento → tier → checkout → pago simulado → confirmación o rechazo.

El repositorio **no reemplaza** ni duplica otras capas de calidad del proyecto; es un componente independiente dentro de una estrategia multicapa.

---

## Posición en la estrategia de calidad

| Capa                       | Herramienta        | Repositorio   |
|----------------------------|--------------------|---------------|
| Funcional UI (E2E — guest) | **Serenity BDD**   | *este repo*   |
| API / Contrato             | Karate             | repo separado |
| Performance                | k6                 | repo separado |

---

## Stack técnico

| Componente       | Versión / Detalle                              |
|------------------|------------------------------------------------|
| Java             | 8+ (validado con 11 y 17)                      |
| Serenity BDD     | 5.3.8                                          |
| Cucumber JVM     | 7.34.2                                         |
| JUnit Platform   | 6.0.3                                          |
| Maven            | 3.8+                                           |
| Navegador        | Google Chrome (driver gestionado automáticamente por WebDriverManager) |
| Patrón de diseño | Screenplay                                     |
| Selector base    | XPath por texto/rol + `data-testid` donde disponible |

---

## Prerrequisitos

1. **Java 8 o superior** con `JAVA_HOME` configurado.
2. **Maven 3.8+** (`mvn -v` para verificar).
3. **Google Chrome** instalado — WebDriverManager descarga el ChromeDriver compatible de forma automática.
4. **Node.js 18+** y el repositorio del frontend del Ticketing MVP disponible localmente.

---

## Cómo levantar el frontend

Los tests requieren que el frontend esté corriendo en **modo E2E**, que activa los botones de pago simulado (`Simular Pago Exitoso` / `Simular Pago Rechazado`) en la pantalla de pago:

```bash
# Desde el repositorio del frontend
npm run dev:e2e
```

Por defecto arranca en `http://localhost:5173`.

> **Sin el modo E2E**, el paso de pago simulado no encontrará los botones mock y los tests fallarán en ese paso. El modo `npm run dev` estándar **no** expone esos botones.

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
        │   ├── guest_happy_path.feature            ← 4 escenarios (1 smoke + 3 outline)
        │   ├── payment_failure.feature             ← 1 escenario (smoke)
        │   ├── ticket_visibility.feature           ← 2 escenarios
        │   └── event_availability.feature          ← 1 escenario (smoke)
        ├── serenity.conf                           ← WebDriver + entornos
        ├── junit-platform.properties               ← Paralelismo y filtros
        └── logback-test.xml                        ← Logging
```

---

## Ejecución de la suite

### Ejecución estándar

```bash
mvn clean test -Dwebdriver.base.url=http://localhost:5173
```

Resultado esperado:

```
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Solo escenarios @smoke

```bash
mvn clean test -Dcucumber.filter.tags="@smoke" \
               -Dwebdriver.base.url=http://localhost:5173
```

### Solo una feature específica

```bash
# Guest happy path
mvn clean test -Dcucumber.filter.tags="@guest-happy-path" \
               -Dwebdriver.base.url=http://localhost:5173

# Pago rechazado
mvn clean test -Dcucumber.filter.tags="@payment-failure" \
               -Dwebdriver.base.url=http://localhost:5173

# Visibilidad de ticket
mvn clean test -Dcucumber.filter.tags="@ticket-visibility" \
               -Dwebdriver.base.url=http://localhost:5173

# Disponibilidad en cartelera
mvn clean test -Dcucumber.filter.tags="@event-availability" \
               -Dwebdriver.base.url=http://localhost:5173
```

### Headless (para CI/CD)

```bash
mvn clean test -Dwebdriver.base.url=http://localhost:5173 \
               -Dheadless.mode=true
```

### Solo compilar sin ejecutar

```bash
mvn test-compile -q
```

---

## Parametrización de URL base

### Por línea de comandos (recomendado)

```bash
mvn clean test -Dwebdriver.base.url=http://localhost:5173
```

### Por entorno declarado en `serenity.conf`

```bash
# Staging
mvn clean test -Denvironment=staging

# Producción (solo lectura; sin modo E2E disponible)
mvn clean test -Denvironment=prod
```

Los entornos están configurados en `src/test/resources/serenity.conf`:

```hocon
environments {
  default {
    webdriver.base.url = "http://localhost:3000"
  }
  dev {
    webdriver.base.url = "http://localhost:3000"
  }
  staging {
    webdriver.base.url = "https://staging.ticketing.example.com"
  }
  prod {
    webdriver.base.url = "https://ticketing.example.com"
  }
}
```

El parámetro `-Dwebdriver.base.url` tiene precedencia sobre cualquier valor declarado en `serenity.conf`.

---

## Cobertura actual

### Estado de ejecución

```
Tests run: 8  |  Failures: 0  |  Errors: 0  |  Skipped: 0
BUILD SUCCESS
```

### Escenarios por feature

#### `guest_happy_path.feature` — `@guest-happy-path @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 1 | Invitado completa el flujo de compra exitosamente de principio a fin | `@smoke` |
| 2 | Invitado elige tier VIP y completa la compra | outline |
| 3 | Invitado elige tier GENERAL y completa la compra | outline |
| 4 | Invitado elige tier EARLY_BIRD y completa la compra | outline |

Flujo cubierto: `/eventos` → tarjeta de evento → selección de tier → Reservar → checkout (email) → Continuar al Pago → Simular Pago Exitoso → pantalla de confirmación.

#### `payment_failure.feature` — `@payment-failure @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 5 | Invitado intenta pagar pero el pago es rechazado y ve la pantalla de fallo con opción de reintento | `@smoke` |

Valida: mensaje "Pago declinado.", presencia del botón "Reintentar Pago".

#### `ticket_visibility.feature` — `@ticket-visibility @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 6 | Invitado visualiza el ticket confirmado tras una compra exitosa | `@smoke` |
| 7 | Invitado con pago rechazado no visualiza el ticket confirmado | — |

Valida: texto "¡Pago aprobado!" visible (escenario positivo); ausencia de ese elemento en pantalla de fallo (escenario negativo).

#### `event_availability.feature` — `@event-availability @mvp`

| # | Escenario | Tags |
|---|-----------|------|
| 8 | La cartelera muestra al menos un evento disponible | `@smoke` |

Valida: existencia de al menos una tarjeta `[data-testid^="event-card-"]` en `/eventos`.

### HUs cubiertas (parcial o total)

| HU | Descripción | Estado en este repo |
|----|-------------|---------------------|
| HU-01 | Visualización de cartelera | ✅ Cubierta (escenario 8) |
| HU-02 | Selección de función / evento | ✅ Cubierta (escenarios 1–4) |
| HU-06 | Flujo de compra como invitado | ✅ Cubierta (escenarios 1–7) |
| HU-03 | Disponibilidad de tiers (agotado, Early Bird vencido) | ⏳ Parcial — requiere datos controlados + auditoría DOM |
| HU-04 | Login de usuario registrado | ❌ No cubierta (fuera del alcance guest) |
| HU-05 | Historial de compras | ❌ No cubierta |
| HU-07 | Administración de eventos | ❌ No cubierta |

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
| Tier agotado no automatizado | Requiere seed data con `stock = 0` en al menos un tier y auditoría del DOM para determinar el selector del estado deshabilitado (`aria-disabled`, clase CSS, `button[disabled]`) |
| Early Bird vencido no automatizado | Requiere seed data con `early_bird_expiry < fecha actual` y auditoría del DOM del estado inactivo del tier |
| Sin flujo autenticado | El repositorio cubre únicamente el flujo de compra como visitante anónimo (guest) |
| Sin validaciones de formulario | No se cubren mensajes de error por email inválido ni campos vacíos |
| Sin tests responsive/móvil | La suite corre en Chrome escritorio 1280×900 |
| Modo E2E obligatorio | Los tests de pago requieren `npm run dev:e2e` en el frontend; no pueden ejecutarse contra el modo de desarrollo estándar |

### Próximos pasos recomendados

1. **HU-03 completa — Tier agotado / Early Bird vencido**
   - Auditar el DOM del frontend con seed data que active esos estados.
   - Actualizar `EventDetailPage.java` con los selectores reales validados.
   - Agregar los 2 escenarios a `event_availability.feature`.

2. **Validaciones de formulario de checkout**
   - Email inválido → mensaje de error visible.
   - Campo vacío → botón "Continuar al Pago" deshabilitado.

3. **CI/CD**
   - Agregar workflow (ej. `.github/workflows/e2e.yml`) con `mvn clean test -Dheadless.mode=true`.
   - Publicar artefacto del reporte Serenity como artefacto del pipeline.

4. **Datos de prueba**
   - Introducir un `TestDataFactory` si la variedad de escenarios lo amerita.
   - Considerar reset de datos vía API antes de escenarios que requieran estado limpio.

---

## Comandos de referencia rápida

```bash
# Ejecución estándar
mvn clean test -Dwebdriver.base.url=http://localhost:5173

# Solo @smoke
mvn clean test -Dcucumber.filter.tags="@smoke" -Dwebdriver.base.url=http://localhost:5173

# Headless
mvn clean test -Dwebdriver.base.url=http://localhost:5173 -Dheadless.mode=true

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
