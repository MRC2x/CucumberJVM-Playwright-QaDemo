# CucumberJVM-Playwright-QaDemo

BDD UI test automation examples for [qademo.com](https://qademo.com), built with Cucumber JVM and Playwright for Java.
Scenarios run in parallel, failures can be retried, and results go to an Allure report with screenshots, videos and Playwright traces. Can be executed on GitHub Actions. 

## Stack

| Area            | Tool                                                         | Version |
|-----------------|--------------------------------------------------------------|---------|
| Language        | Java                                                         | 21      |
| Build           | Gradle (wrapper)                                             | 9.8.2   |
| Browser driver  | [Playwright for Java](https://playwright.dev/java/)          | 1.62.0  |
| BDD             | [Cucumber JVM](https://cucumber.io/docs/installation/java/)  | 7.34.2  |
| Test platform   | JUnit 5 Platform Suite                                       | 5.14.2  |
| Reporting       | [Allure 3](https://allurereport.org/)                        | 3.x     |
| CI              | GitHub Actions + GitHub Pages (report hosting)               |         |

## Project structure

```
src
├── main/java
│   ├── hooks/                  # Cucumber hooks (setup/teardown, failure attachments), Page DI provider
│   ├── infrastructure/
│   │   ├── config/             # browser launch config (browser type, headless)
│   │   ├── constants/          # timeouts, runtime parameters (-D...)
│   │   ├── testContext/        # per-scenario shared state
│   │   └── utilities/          # Allure helpers, asserters, logging, Playwright session/lifecycle
│   └── pageObject/
│       ├── components/         # reusable UI components (navigation bar, product card)
│       └── pages/              # page objects (Home, Catalog, Product, Cart)
└── test
    ├── java
    │   ├── CucumberRunner.java       # main suite runner
    │   ├── CucumberRetryRunner.java  # used by retry tasks only, don't run directly
    │   └── steps/                    # step definitions
    └── resources
        ├── allure.properties
        └── features/                 # .feature files
```

Design notes:
- **One browser per thread, one context per scenario.** Each worker thread reuses its own `Browser`; every scenario gets a fresh `BrowserContext` (`BrowserSession`), so scenarios are isolated and safe to run in parallel.
- **Dependency injection** via PicoContainer: page objects, `Page`, `BrowserSession` and `TestContext` are injected into step classes through constructors.
- **API sign-in.** "Signed-in user" steps log in through `/api/auth/login` and seed the session into the context, skipping the login UI.

## Prerequisites

- JDK 21
- No Gradle install needed, use the wrapper (`./gradlew`)
- Playwright downloads browser binaries automatically on the first run

## Running locally

Run all scenarios (Chromium, headed, parallel):

```bash
./gradlew clean test -Dlogin=<user> -Dpassword=<password>
```

Run with parameters:

```bash
# a single scenario by tag, headless Firefox
./gradlew clean test -Dcucumber.filter.tags="@TST-1" -Dbrowser=firefox -Dheadless=true -Dlogin=<user> -Dpassword=<password>

# a whole feature, sequentially
./gradlew clean test -Dcucumber.filter.tags="@Checkout" -Dparallel.execution=false -Dlogin=<user> -Dpassword=<password>

# 4 threads, retry failed scenarios up to 2 times, record video
./gradlew clean test -Dthreads=4 -Dretry=2 -Dvideo=true -Dlogin=<user> -Dpassword=<password>
```

`login` and `password` are only required by scenarios that use a signed-in user; the run fails fast with a clear message if they are missing.

> The `test` task has `ignoreFailures = true`, so the Gradle build stays green even when scenarios fail. Check the summary printed at the end of the run or the Allure report.

## Parameters

All parameters are passed as JVM system properties: `-D<name>=<value>`.

| Parameter               | Default                | Description                                                                                                  |
|-------------------------|------------------------|--------------------------------------------------------------------------------------------------------------|
| `browser`               | `chromium`             | Browser to run in: `chromium`, `firefox`, `webkit`                                                           |
| `headless`              | `false`                | Run the browser without UI                                                                                    |
| `cucumber.filter.tags`  | _(all)_                | Cucumber [tag expression](https://cucumber.io/docs/cucumber/api/#tag-expressions), e.g. `@TST-1`, `@Checkout and not @TST-2` |
| `parallel.execution`    | `true`                 | Run scenarios in parallel                                                                                     |
| `threads`               | half of the CPU cores (min 1) | Number of parallel threads                                                                             |
| `retry`                 | `0`                    | How many times to re-run failed scenarios (each attempt re-runs only the scenarios still failing)            |
| `video`                 | `false`                | Record video; attached to the report for failed scenarios                                                     |
| `trace`                 | `true`                 | Record a Playwright trace; attached to the report for failed scenarios                                        |
| `login`                 | —                      | Test user login, required for signed-in scenarios                                                             |
| `password`              | —                      | Test user password, required for signed-in scenarios                                                         |

Scenarios tagged `@Ignore`, `@wip` or `@knownbug` are always excluded.

## Running on CI

Tests run on GitHub Actions via the manually triggered [**Web tests - Manual Execution**](.github/workflows/manual-run.yml) workflow:
**Actions → Web tests - Manual Execution → Run workflow**.

| Input       | Default    | Description                                                         |
|-------------|------------|---------------------------------------------------------------------|
| `BROWSER`   | `chromium` | `chromium`, `firefox` or `webkit`                                   |
| `TEST_TAG`  | _(empty)_  | Cucumber tag expression, e.g. `@TST-1` or `@Checkout`; empty = all  |
| `PARALLEL`  | `true`     | Parallel execution                                                  |
| `THREADS`   | `2`        | Number of threads (1–4), parallel execution only                    |

The workflow:
1. Runs the tests headless inside the `mcr.microsoft.com/playwright/java` container.
2. Uploads the raw Allure results as a build artifact (kept for 3 days).
3. Generates the Allure report and publishes it to GitHub Pages, one folder per run.
4. Posts a results summary table and the report link to the run summary.

Required repository secrets:

| Secret               | Description         |
|----------------------|---------------------|
| `TEST_USER_LOGIN`    | Test user login     |
| `TEST_USER_PASSWORD` | Test user password  |

These are used to run signed-in scenarios and carry my test user credentials. The workflow fails fast with a clear message if they are missing.

GitHub Pages must be enabled for the `gh-pages` branch (**Settings → Pages → Deploy from a branch**).

> When bumping the Playwright version in `build.gradle`, update the container image tag in the workflow to match.

## Test report

Results are written to `build/allure-results`. For failed scenarios the report contains:
- a screenshot of every open page
- a Playwright trace, viewable right in the report (Allure 3 trace viewer)
- a video, when run with `-Dvideo=true`

Retried scenarios are grouped together with their previous attempts.

**Locally**, generate and open the report with Allure 3 (requires Node.js):

```bash
npx allure generate build/allure-results -o build/allure-report
npx allure open build/allure-report
```

A trace can also be downloaded from the report and opened with `npx playwright show-trace <trace.zip>`.

**On CI**, every run is published to GitHub Pages (with some delay):

```
https://mrc2x.github.io/CucumberJVM-Playwright-QaDemo/<run_number>/
```

The direct link should appear in the workflow run summary.