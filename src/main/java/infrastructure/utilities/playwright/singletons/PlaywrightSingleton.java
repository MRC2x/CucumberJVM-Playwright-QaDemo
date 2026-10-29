package infrastructure.utilities.playwright.singletons;

import com.microsoft.playwright.Playwright;
import lombok.extern.log4j.Log4j2;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j2
public class PlaywrightSingleton {

    private static final ThreadLocal<Playwright> instance = new ThreadLocal<>();
    // every thread's instance, so they all can be closed from the @AfterAll thread
    private static final Queue<Playwright> allInstances = new ConcurrentLinkedQueue<>();

    private PlaywrightSingleton() {}

    public static Playwright getInstance() {
        if (instance.get() == null) {
            log.info("Playwright instance initializing...");
            Playwright playwright = Playwright.create();
            playwright.selectors().setTestIdAttribute("data-testid");
            instance.set(playwright);
            allInstances.add(playwright);
        }

        return instance.get();
    }

    public static void closeAll() {
        Playwright playwright;
        while ((playwright = allInstances.poll()) != null) {
            try {
                playwright.close();
            } catch (Exception e) {
                log.warn("Failed to close Playwright instance", e);
            }
        }
        instance.remove();
    }
}
