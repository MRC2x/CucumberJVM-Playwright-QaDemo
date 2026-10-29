package infrastructure.utilities.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import lombok.extern.log4j.Log4j2;
import org.picocontainer.Disposable;

import java.nio.file.Path;

import static infrastructure.constants.Timeouts.DEFAULT_TIMEOUT;
import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;

/**
 * Per-scenario browser state: one {@link BrowserContext} and its initial {@link Page}.
 * Managed by PicoContainer, so a fresh instance is created on the scenario's thread for every scenario
 * and disposed when the scenario ends. The thread's {@link Browser} is reused across scenarios.
 */
@Log4j2
public class BrowserSession implements Disposable {

    private final BrowserContext context;
    private final Page page;
    private final boolean tracing;
    private boolean closed = false;

    public BrowserSession() {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setBaseURL("https://qademo.com")
                .setLocale("en-US")
                .setIgnoreHTTPSErrors(true)
                .setAcceptDownloads(true)
                .setViewportSize(1920, 1200)
                .setRecordVideoDir(parseBoolean(getProperty("video")) ? Path.of("./video") : null);

        log.info("Browser Context initializing...");
        context = PlaywrightManager.browser().newContext(contextOptions);
        context.setDefaultTimeout(DEFAULT_TIMEOUT.toMillis());

        // tracing is on by default, disable with -Dtrace=false
        tracing = parseBoolean(getProperty("trace", "true"));
        if (tracing)
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));

        page = context.newPage();
    }

    public BrowserContext context() {
        return context;
    }

    public Page page() {
        return page;
    }

    public boolean isTracing() {
        return tracing;
    }

    /**
     * Stops tracing and closes the context. Closing the context flushes recorded videos to disk.
     *
     * @param tracePath where to save the trace, or {@code null} to discard it
     */
    public void close(Path tracePath) {
        if (closed) return;
        closed = true;

        try {
            if (tracing)
                context.tracing().stop(tracePath != null ? new Tracing.StopOptions().setPath(tracePath) : null);
        } finally {
            context.close();
            log.info("Browser Context closed!");
        }
    }

    /**
     * Safety net: called by PicoContainer when the scenario ends, in case the @After hook didn't close the context.
     */
    @Override
    public void dispose() {
        close(null);
    }
}
