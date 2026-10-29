import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Retry runner for failed Cucumber scenarios.
 * Feature files are specified at runtime via the {@code cucumber.features} system property,
 * which receives a comma-separated list of failed scenario URIs passed directly by the
 * Gradle {@code retryAttemptN} tasks (e.g., {@code path/to/feature.feature:42,path/to/other.feature:17}).
 * Note: {@code @file} expansion is CLI-only and is NOT used here — the JUnit Platform engine
 * does not support it.
 * This runner is invoked automatically by the retryAttemptN Gradle tasks — do not run it directly.
 */
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps,hooks")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, rerun:build/cucumber-rerun.txt"
)
@ExcludeTags({"Ignore", "wip", "knownbug"})
public class CucumberRetryRunner {
}
