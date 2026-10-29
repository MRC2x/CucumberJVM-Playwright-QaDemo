package infrastructure.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import static infrastructure.constants.WebEnvParams.getBrowserParam;


public class BrowserConfig {

    private final String browserType;
    private final boolean headless;

    public BrowserConfig() {
        this.browserType = getBrowserParam();
        this.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    public Browser launchBrowser(Playwright playwright) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless);

        return switch (browserType) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    public static BrowserConfig getConfig() {
        return new BrowserConfig();
    }
}