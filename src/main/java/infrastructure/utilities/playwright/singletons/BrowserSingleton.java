package infrastructure.utilities.playwright.singletons;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import infrastructure.config.BrowserConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static infrastructure.constants.WebEnvParams.getBrowserParam;

@Log4j2
public class BrowserSingleton {

    private static final ThreadLocal<Browser> instance = new ThreadLocal<>();
    // every thread's instance, so they all can be closed from the @AfterAll thread
    private static final Queue<Browser> allInstances = new ConcurrentLinkedQueue<>();

    private BrowserSingleton() {}

    public static Browser getInstance() {

        if (instance.get() == null) {
            BrowserConfig config = BrowserConfig.getConfig();
            Playwright playwright = PlaywrightSingleton.getInstance();

            log.info("{} browser instance initializing...", StringUtils.capitalize(getBrowserParam()));
            Browser browser = config.launchBrowser(playwright);
            instance.set(browser);
            allInstances.add(browser);
        }

        return instance.get();
    }

    public static void closeAll() {
        Browser browser;
        while ((browser = allInstances.poll()) != null) {
            try {
                browser.close();
            } catch (Exception e) {
                log.warn("Failed to close Browser instance", e);
            }
        }
        instance.remove();
    }
}
