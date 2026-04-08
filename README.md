# Ticketing MVP — Automatización Funcional UI con Serenity BDD + Cucumber

> **Repositorio de automatización funcional E2E** para el proyecto **Ticketing MVP** (venta de entradas de teatro).  
> Stack: Java · Serenity BDD · Cucumber · Screenplay Pattern · Maven  
> Basado en la plantilla oficial: [serenity-bdd/serenity-cucumber-starter](https://github.com/serenity-bdd/serenity-cucumber-starter)

---

## Propósito

Este repositorio cubre la **automatización funcional de la UI** del Ticketing MVP.  
Es un componente independiente dentro de la estrategia de calidad del proyecto, que convive con:

| Capa                  | Herramienta       | Repositorio    |
|-----------------------|-------------------|----------------|
| Funcional UI (E2E)    | **Serenity BDD**  | *este repo*    |
| API / Contrato        | Karate            | repo separado  |
| Performance           | k6                | repo separado  |

---

## Stack Técnico

| Componente       | Versión / Detalle                         |
|------------------|-------------------------------------------|
| Java             | 8+ (compatible con 11 y 17)               |
| Serenity BDD     | 5.3.8                                     |
| Cucumber JVM     | 7.34.2                                    |
| JUnit Platform   | 6.0.3                                     |
| Maven            | 3.8+                                      |
| Navegador        | Chrome (vía WebDriverManager automático)  |
| Patrón           | Screenplay                                |

---

## Prerrequisitos

1. **Java 8 o superior** instalado y `JAVA_HOME` configurado.
2. **Maven 3.8+** instalado (`mvn -v` para verificar).
3. **Google Chrome** instalado (el WebDriverManager descarga el driver automáticamente).
4. **Node.js 18+** y el frontend de Ticketing MVP disponible localmente.

---

## Cómo levantar el frontend en modo E2E

El frontend del Ticketing MVP debe ejecutarse en **modo E2E** para que el botón de
pago simulado (`[data-testid="mock-payment-success"]`) esté disponible:

```bash
# Desde el repositorio del frontend
npm run dev:e2e
```

Por defecto arranca en `http://localhost:3000`.

> ⚠️ **Sin el modo E2E**, el paso de pago simulado no tendrá el botón `mock-payment-success`
> y los tests fallarán en ese paso.

---

## Estructura del Proyecto

```
src/
├── test/
│   ├── java/
│   │   └── ticketing/
│   │       ├── CucumberTestSuite.java              ← Runner principal (JUnit Platform Suite)
│   │       ├── navigation/
│   │       │   └── NavigateTo.java                 ← Tareas de navegación (open URL)
│   │       ├── screenplay/
│   │       │   ├── tasks/
│   │       │   │   ├── SelectEvent.java             ← Clic en la primera tarjeta de evento
│   │       │   │   ├── SelectTier.java              ← Clic en el tier (VIP/GENERAL/EARLY_BIRD)
│   │       │   │   ├── ReserveTicket.java           ← Clic en "Reservar"
│   │       │   │   ├── EnterEmail.java              ← Escribe el email del invitado
│   │       │   │   ├── ContinueToPayment.java       ← Clic en "Continuar al pago"
│   │       │   │   └── CompletePayment.java         ← Clic en el mock de pago exitoso
│   │       │   ├── questions/
│   │       │   │   └── SuccessScreen.java           ← Verifica la pantalla de confirmación
│   │       │   └── ui/                              ← Locators (data-testid validados)
│   │       │       ├── EventsPage.java              ← [data-testid^="event-card-"]
│   │       │       ├── EventDetailPage.java         ← tier-VIP/GENERAL/EARLY_BIRD, reserve-tier-btn
│   │       │       ├── CheckoutPage.java            ← checkout-email-input, payment-continue-btn
│   │       │       ├── PaymentPage.java             ← mock-payment-success
│   │       │       └── ConfirmationPage.java        ← success-title
│   │       └── stepdefinitions/
│   │           ├── ParameterDefinitions.java        ← Registra el tipo {actor} + @Before stage
│   │           └── GuestPurchaseStepDefinitions.java ← Glue code del Guest Happy Path
│   └── resources/
│       ├── features/
│       │   └── ticketing/
│       │       └── guest_happy_path.feature         ← Feature principal del MVP
│       ├── serenity.conf                            ← Configuración WebDriver + entornos
│       ├── junit-platform.properties               ← Paralelismo y filtros Cucumber
│       └── logback-test.xml                        ← Logging
```

---

## Cómo Ejecutar los Tests

### Ejecución estándar (con frontend en localhost:3000)

```bash
mvn clean verify
```

### Con headless mode (para CI/CD)

```bash
mvn clean verify -Dheadless.mode=true
```

### Solo los tests @smoke

```bash
mvn clean verify -Dcucumber.filter.tags="@smoke"
```

### Solo el Guest Happy Path completo

```bash
mvn clean verify -Dcucumber.filter.tags="@guest-happy-path"
```

### Ver el reporte HTML de Serenity

```bash
mvn clean verify
# El reporte se genera en:
open target/site/serenity/index.html
```

---

## Cómo Cambiar la URL Base

### Opción 1 — Parámetro Maven en línea de comandos

```bash
mvn clean verify -Dwebdriver.base.url=http://localhost:3001
```

### Opción 2 — Variable de entorno en serenity.conf

Editar `src/test/resources/serenity.conf`:

```hocon
environments {
  default {
    webdriver.base.url = "http://localhost:3000"   # ← cambiar aquí
  }
  staging {
    webdriver.base.url = "https://staging.ticketing.example.com"
  }
}
```

Luego ejecutar con:

```bash
mvn clean verify -Denvironment=staging
```

### Opción 3 — Entorno CI/CD

```bash
mvn clean verify \
  -Dwebdriver.base.url=$TICKETING_BASE_URL \
  -Dheadless.mode=true
```

---

## Selectores Validados (data-testid)

| Elemento                    | Selector                                   |
|-----------------------------|--------------------------------------------|
| Tarjeta de evento           | `[data-testid^="event-card-"]`             |
| Botón hero de compra        | `[data-testid="hero-buy-btn"]`             |
| Tier VIP                    | `[data-testid="tier-VIP"]`                 |
| Tier GENERAL                | `[data-testid="tier-GENERAL"]`             |
| Tier EARLY_BIRD             | `[data-testid="tier-EARLY_BIRD"]`          |
| Botón reservar              | `[data-testid="reserve-tier-btn"]`         |
| Campo email checkout        | `[data-testid="checkout-email-input"]`     |
| Continuar al pago           | `[data-testid="payment-continue-btn"]`     |
| Simular pago exitoso (mock) | `[data-testid="mock-payment-success"]`     |
| Título de confirmación      | `[data-testid="success-title"]`            |

> Todos estos selectores fueron validados durante la auditoría del frontend del Ticketing MVP.

---

## Qué Cubre Este Repositorio (hoy)

- ✅ **Guest Happy Path completo**: cartelera → evento → tier → reservar → email → pago → confirmación
- ✅ Tres variantes de tier: VIP, GENERAL, EARLY_BIRD
- ✅ Esperas explícitas (sin sleeps)
- ✅ Reporte Serenity BDD con capturas de pantalla en fallos
- ✅ Parametrización de URL base por entorno
- ✅ Configuración lista para Chrome headless y modo visible

---

## Qué NO Cubre Todavía

| HU / Flujo                     | Estado      |
|-------------------------------|-------------|
| HU-03 — Registro de usuario   | No iniciado |
| HU-04 — Login de usuario      | No iniciado |
| HU-07 — Historial de compras  | No iniciado |
| Pago fallido / rechazado      | No iniciado |
| Validaciones de formulario    | No iniciado |
| Flujo autenticado             | No iniciado |
| Responsive / móvil            | No iniciado |

---

## Próximos Pasos para Escalar

1. **HU-03 – Registro**: Agregar `features/ticketing/registro_usuario.feature` + tasks de formulario de registro.
2. **HU-04 – Login**: Agregar `features/ticketing/login_usuario.feature` + task `Login.withCredentials(user, password)`.
3. **HU-07 – Historial**: Agregar `features/ticketing/historial_compras.feature` + locators de la vista de historial.
4. **Pago fallido**: Agregar scenario en el mismo feature con tag `@pago-fallido` y un botón `mock-payment-failure`.
5. **Page Objects adicionales**: Crear `ProfilePage.java` y `HistoryPage.java` en `screenplay/ui/`.
6. **Datos de prueba**: Introducir un `TestDataFactory` para gestionar emails únicos por ejecución.
7. **CI/CD**: Agregar `.github/workflows/e2e.yml` con `mvn clean verify -Dheadless.mode=true`.

---

## Comandos de Referencia Rápida

```bash
# Instalar dependencias y compilar
mvn clean compile -T 4

# Ejecutar todos los tests
mvn clean verify

# Ejecutar solo @smoke en headless
mvn clean verify -Dcucumber.filter.tags="@smoke" -Dheadless.mode=true

# Generar solo el reporte (si ya existen resultados)
mvn serenity:aggregate

# Limpiar resultados anteriores
mvn clean
```

---

## Referencias

- [Serenity BDD Docs](https://serenity-bdd.github.io/theserenitybook/latest/)
- [Template oficial: serenity-cucumber-starter](https://github.com/serenity-bdd/serenity-cucumber-starter)
- [Screenplay Pattern Guide](https://serenity-bdd.github.io/theserenitybook/latest/screenplay.html)
- [Cucumber JVM Docs](https://cucumber.io/docs/cucumber/)


Serenity BDD is a library that makes it easier to write high quality automated acceptance tests, with powerful reporting and living documentation features. It has strong support for both web testing with Selenium, and API testing using RestAssured.

Serenity strongly encourages good test automation design, and supports several design patterns, including classic Page Objects, the newer Lean Page Objects/ Action Classes approach, and the more sophisticated and flexible Screenplay pattern.

The latest version of Serenity supports Cucumber 6.x.

## The starter project
The best place to start with Serenity and Cucumber is to clone or download the starter project on Github ([https://github.com/serenity-bdd/serenity-cucumber-starter](https://github.com/serenity-bdd/serenity-cucumber-starter)). This project gives you a basic project setup, along with some sample tests and supporting classes. There are two versions to choose from. The master branch uses a more classic approach, using action classes and lightweight page objects, whereas the **[screenplay](https://github.com/serenity-bdd/serenity-cucumber-starter/tree/screenplay)** branch shows the same sample test implemented using Screenplay.

### The project directory structure
The project has build scripts for both Maven and Gradle, and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                        Test runners and supporting code
    + resources
      + features                  Feature files
     + search                  Feature file subdirectories 
             search_by_keyword.feature
```

Serenity 2.2.13 introduced integration with WebdriverManager to download webdriver binaries.

## The sample scenario
Both variations of the sample project uses the sample Cucumber scenario. In this scenario, Sergey (who likes to search for stuff) is performing a search on the internet:

```Gherkin
Feature: Search by keyword

  Scenario: Searching for a term
    Given Sergey is researching things on the internet
    When he looks up "Cucumber"
    Then he should see information about "Cucumber"
```

### The Screenplay implementation
The sample code in the master branch uses the Screenplay pattern. The Screenplay pattern describes tests in terms of actors and the tasks they perform. Tasks are represented as objects performed by an actor, rather than methods. This makes them more flexible and composable, at the cost of being a bit more wordy. Here is an example:
```java
    @Given("{actor} is researching things on the internet")
    public void researchingThings(Actor actor) {
        actor.wasAbleTo(NavigateTo.theWikipediaHomePage());
    }

    @When("{actor} looks up {string}")
    public void searchesFor(Actor actor, String term) {
        actor.attemptsTo(
                LookForInformation.about(term)
        );
    }

    @Then("{actor} should see information about {string}")
    public void should_see_information_about(Actor actor, String term) {
        actor.attemptsTo(
                Ensure.that(WikipediaArticle.HEADING).hasText(term)
        );
    }
```

Screenplay classes emphasise reusable components and a very readable declarative style, whereas Lean Page Objects and Action Classes (that you can see further down) opt for a more imperative style.

The `NavigateTo` class is responsible for opening the Wikipedia home page:
```java
public class NavigateTo {
    public static Performable theWikipediaHomePage() {
        return Task.where("{0} opens the Wikipedia home page",
                Open.browserOn().the(WikipediaHomePage.class));
    }
}
```

The `LookForInformation` class does the actual search:
```java
public class LookForInformation {
    public static Performable about(String searchTerm) {
        return Task.where("{0} searches for '" + searchTerm + "'",
                Enter.theValue(searchTerm)
                        .into(SearchForm.SEARCH_FIELD)
                        .thenHit(Keys.ENTER)
        );
    }
}
```

In Screenplay, we keep track of locators in light weight page or component objects, like this one:
```java
class SearchForm {
    static Target SEARCH_FIELD = Target.the("search field")
                                       .locatedBy("#searchInput");

}
```

The Screenplay DSL is rich and flexible, and well suited to teams working on large test automation projects with many team members, and who are reasonably comfortable with Java and design patterns. 

### The Action Classes implementation.

A more imperative-style implementation using the Action Classes pattern can be found in the `action-classes` branch. The glue code in this version looks this this:

```java
    @Given("^(?:.*) is researching things on the internet")
    public void i_am_on_the_Wikipedia_home_page() {
        navigateTo.theHomePage();
    }

    @When("she/he looks up {string}")
    public void i_search_for(String term) {
        searchFor.term(term);
    }

    @Then("she/he should see information about {string}")
    public void all_the_result_titles_should_contain_the_word(String term) {
        assertThat(searchResult.displayed()).contains(term);
    }
```

These classes are declared using the Serenity `@Steps` annotation, shown below:
```java
    @Steps
    NavigateTo navigateTo;

    @Steps
    SearchFor searchFor;

    @Steps
    SearchResult searchResult;
```

The `@Steps`annotation tells Serenity to create a new instance of the class, and inject any other steps or page objects that this instance might need.

Each action class models a particular facet of user behaviour: navigating to a particular page, performing a search, or retrieving the results of a search. These classes are designed to be small and self-contained, which makes them more stable and easier to maintain.

The `NavigateTo` class is an example of a very simple action class. In a larger application, it might have some other methods related to high level navigation, but in our sample project, it just needs to open the DuckDuckGo home page:
```java
public class NavigateTo {

    WikipediaHomePage homePage;

    @Step("Open the Wikipedia home page")
    public void theHomePage() {
        homePage.open();
    }
}
```

It does this using a standard Serenity Page Object. Page Objects are often very minimal, storing just the URL of the page itself:
```java
@DefaultUrl("https://wikipedia.org")
public class WikipediaHomePage extends PageObject {}
```

The second class, `SearchFor`, is an interaction class. It needs to interact with the web page, and to enable this, we make the class extend the Serenity `UIInteractionSteps`. This gives the class full access to the powerful Serenity WebDriver API, including the `$()` method used below, which locates a web element using a `By` locator or an XPath or CSS expression:
```java
public class SearchFor extends UIInteractionSteps {

    @Step("Search for term {0}")
    public void term(String term) {
        $(SearchForm.SEARCH_FIELD).clear();
        $(SearchForm.SEARCH_FIELD).sendKeys(term, Keys.ENTER);
    }
}
```

The `SearchForm` class is typical of a light-weight Page Object: it is responsible uniquely for locating elements on the page, and it does this by defining locators or occasionally by resolving web elements dynamically.
```java
class SearchForm {
    static By SEARCH_FIELD = By.cssSelector("#searchInput");
}
```

The last step library class used in the step definition code is the `SearchResult` class. The job of this class is to query the web page, and retrieve a list of search results that we can use in the AssertJ assertion at the end of the test. This class also extends `UIInteractionSteps` and
```java
public class SearchResult extends UIInteractionSteps {
    public String displayed() {
        return find(WikipediaArticle.HEADING).getText();
    }
}
```

The `WikipediaArticle` class is a lean Page Object that locates the article titles on the results page:
```java
public class WikipediaArticle {
    public static final By HEADING =  By.id("firstHeading");
}
```

The main advantage of the approach used in this example is not in the lines of code written, although Serenity does reduce a lot of the boilerplate code that you would normally need to write in a web test. The real advantage is in the use of many small, stable classes, each of which focuses on a single job. This application of the _Single Responsibility Principle_ goes a long way to making the test code more stable, easier to understand, and easier to maintain.

## Executing the tests
To run the sample project, you can either just run the `CucumberTestSuite` test runner class, or run either `mvn verify` or `gradle test` from the command line.

By default, the tests will run using Chrome. You can run them in Firefox by overriding the `driver` system property, e.g.
```json
$ mvn clean verify -Ddriver=firefox
```
Or
```json
$ gradle clean test -Pdriver=firefox
```

The test results will be recorded in the `target/site/serenity` directory.

## Generating the reports
Since the Serenity reports contain aggregate information about all of the tests, they are not generated after each individual test (as this would be extremenly inefficient). Rather, The Full Serenity reports are generated by the `serenity-maven-plugin`. You can trigger this by running `mvn serenity:aggregate` from the command line or from your IDE.

They reports are also integrated into the Maven build process: the following code in the `pom.xml` file causes the reports to be generated automatically once all the tests have completed when you run `mvn verify`?

```
             <plugin>
                <groupId>net.serenity-bdd.maven.plugins</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.maven.version}</version>
                <configuration>
                    <tags>${tags}</tags>
                </configuration>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

## Simplified WebDriver configuration and other Serenity extras
The sample projects both use some Serenity features which make configuring the tests easier. In particular, Serenity uses the `serenity.conf` file in the `src/test/resources` directory to configure test execution options.  
### Webdriver configuration
The WebDriver configuration is managed entirely from this file, as illustrated below:
```java
webdriver {
    driver = chrome
}
headless.mode = true

chrome.switches="""--start-maximized;--test-type;--no-sandbox;--ignore-certificate-errors;
                   --disable-popup-blocking;--disable-default-apps;--disable-extensions-file-access-check;
                   --incognito;--disable-infobars,--disable-gpu"""

```

Serenity uses WebDriverManager to download the WebDriver binaries automatically before the tests are executed.

### Environment-specific configurations
We can also configure environment-specific properties and options, so that the tests can be run in different environments. Here, we configure three environments, __dev__, _staging_ and _prod_, with different starting URLs for each:
```json
environments {
  default {
    webdriver.base.url = "https://duckduckgo.com"
  }
  dev {
    webdriver.base.url = "https://duckduckgo.com/dev"
  }
  staging {
    webdriver.base.url = "https://duckduckgo.com/staging"
  }
  prod {
    webdriver.base.url = "https://duckduckgo.com/prod"
  }
}
```

You use the `environment` system property to determine which environment to run against. For example to run the tests in the staging environment, you could run:
```json
$ mvn clean verify -Denvironment=staging
```

See [**this article**](https://johnfergusonsmart.com/environment-specific-configuration-in-serenity-bdd/) for more details about this feature.

## Want to learn more?
For more information about Serenity BDD, you can read the [**Serenity BDD Book**](https://serenity-bdd.github.io/theserenitybook/latest/index.html), the official online Serenity documentation source. Other sources include:
* **[Learn Serenity BDD Online](https://expansion.serenity-dojo.com/)** with online courses from the Serenity Dojo Training Library
* **[Byte-sized Serenity BDD](https://www.youtube.com/channel/UCav6-dPEUiLbnu-rgpy7_bw/featured)** - tips and tricks about Serenity BDD
* For regular posts on agile test automation best practices, join the **[Agile Test Automation Secrets](https://www.linkedin.com/groups/8961597/)** groups on [LinkedIn](https://www.linkedin.com/groups/8961597/) and [Facebook](https://www.facebook.com/groups/agiletestautomation/)
* [**Serenity BDD Blog**](https://johnfergusonsmart.com/category/serenity-bdd/) - regular articles about Serenity BDD
