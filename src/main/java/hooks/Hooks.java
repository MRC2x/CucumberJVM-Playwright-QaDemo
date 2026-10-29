package hooks;

import infrastructure.testContext.TestContext;
import infrastructure.utilities.playwright.BrowserSession;
import infrastructure.utilities.playwright.singletons.BrowserSingleton;
import infrastructure.utilities.playwright.singletons.PlaywrightSingleton;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.*;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static infrastructure.constants.Timeouts.*;

import static infrastructure.utilities.allure.AllureUtils.allureAttachScreenshot;
import static infrastructure.utilities.allure.AllureUtils.allureAttachTrace;
import static infrastructure.utilities.allure.AllureUtils.allureAttachVideo;
import static infrastructure.utilities.allure.AllureUtils.fileToInputStream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;


@RequiredArgsConstructor
public class Hooks {

    private final TestContext testContext;
    private final BrowserSession session;

    @BeforeAll
    public static void beforeAll() {
        // set timeout for all default Playwright assertions
        PlaywrightAssertions.setDefaultAssertionTimeout(LONG_TIMEOUT.toMillis());
    }

    @Before
    public void captureScenario(Scenario scenario) {
        testContext.setScenario(scenario);
    }

    @After
    public void tearDown(Scenario scenario) {
        List<String> videoPaths = new ArrayList<>();
        Path tracePath = null;

        try {
            if (scenario.isFailed()) {
                session.context().pages().forEach(page -> {
                    allureAttachScreenshot("Failure screenshot", page.screenshot());

                    // video path must be collected before context.close() flushes the video to disk
                    if (parseBoolean(getProperty("video")))
                        videoPaths.add(page.video().path().toString());
                });

                if (session.isTracing())
                    tracePath = tracePathFor(scenario);
            }
        } finally {
            // stops tracing (saving the trace only on failure) and closes the context,
            // which flushes video to disk — must happen before attaching videos
            session.close(tracePath);

            videoPaths.forEach(path ->
                    allureAttachVideo("Failure video", fileToInputStream(path))
            );

            if (tracePath != null)
                allureAttachTrace("Failure trace", fileToInputStream(tracePath.toString()));
        }
    }

    @AfterAll
    public static void afterAll() {
        // @AfterAll runs on a single thread, while each worker thread owns its own Browser/Playwright
        BrowserSingleton.closeAll();
        PlaywrightSingleton.closeAll();
    }

    private static Path tracePathFor(Scenario scenario) {
        String fileName = scenario.getName().replaceAll("[^A-Za-z0-9._-]+", "_") + "_" + scenario.getLine();
        return Path.of("build", "traces", fileName + ".zip");
    }

}
