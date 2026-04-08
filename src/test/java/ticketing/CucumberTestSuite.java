package ticketing;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Runner principal de Cucumber para el proyecto Ticketing MVP.
 *
 * Ejecución estándar:
 *   mvn clean verify
 *
 * Filtrar por tag:
 *   mvn clean verify -Dcucumber.filter.tags="@guest-happy-path"
 *
 * Cambiar entorno:
 *   mvn clean verify -Denvironment=staging
 *
 * Cambiar baseUrl directamente:
 *   mvn clean verify -Dwebdriver.base.url=http://localhost:3001
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel,pretty,timeline:build/test-results/timeline"
)
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "ticketing.stepdefinitions"
)
public class CucumberTestSuite {
    // Clase vacía – JUnit Platform Suite lanza Cucumber automáticamente.
}
