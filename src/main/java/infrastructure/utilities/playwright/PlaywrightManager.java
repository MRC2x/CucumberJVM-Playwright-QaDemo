package infrastructure.utilities.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import infrastructure.utilities.playwright.singletons.BrowserSingleton;
import infrastructure.utilities.playwright.singletons.PlaywrightSingleton;

/**
 * Per-thread Playwright and Browser instances.
 * Per-scenario BrowserContext and Page are provided by {@link BrowserSession}.
 */
public class PlaywrightManager {

    public static Playwright playwright() {
        return PlaywrightSingleton.getInstance();
    }

    public static Browser browser() {
        return BrowserSingleton.getInstance();
    }

}
