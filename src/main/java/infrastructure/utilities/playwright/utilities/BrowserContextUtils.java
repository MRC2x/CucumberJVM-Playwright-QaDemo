package infrastructure.utilities.playwright.utilities;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.util.List;

/**
 * Utility class for the initial browser context
 */
public final class BrowserContextUtils {

    private BrowserContextUtils() {}

    /**
     * @param context browser context to add the cookie into
     * @param cookie cookie to add into browser context
     */
    public static void addCookie(BrowserContext context, Cookie cookie) {
        context.addCookies(List.of(cookie));
    }

    /**
     * @param context browser context to add the cookie into
     * @param name name of cookie to add into browser context
     * @param value value of cookie to add into browser context
     */
    public static void addCookie(BrowserContext context, String name, String value) {
        Cookie cookie = new Cookie(name, value)
                .setDomain("qademo.com")
                .setPath("/");

        addCookie(context, cookie);
    }

}
