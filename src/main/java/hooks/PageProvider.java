package hooks;

import com.microsoft.playwright.Page;
import infrastructure.utilities.playwright.BrowserSession;
import io.cucumber.picocontainer.CucumberPicoProvider;
import org.picocontainer.injectors.Provider;

/**
 * Lets PicoContainer inject {@link Page} into page objects and step classes.
 * The page belongs to the current scenario's {@link BrowserSession}.
 */
@CucumberPicoProvider
public class PageProvider implements Provider {

    public Page provide(BrowserSession session) {
        return session.page();
    }
}
