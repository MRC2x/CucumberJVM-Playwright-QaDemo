package infrastructure.utilities.playwright.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.RequestOptions;

import java.util.List;
import java.util.Map;

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

    /**
     * Signs the user in via API, skipping the login UI. Must be called before the first navigation.
     * <p>
     * The app restores a session on page load only when both are present:
     * the HttpOnly {@code refresh_token} cookie (set into the context by the login response)
     * and the {@code auth-storage} localStorage entry (seeded by an init script).
     *
     * @param context browser context to sign the user into
     * @param login user's login (username or email)
     * @param password user's password
     */
    public static void signIn(BrowserContext context, String login, String password) {
        APIResponse response = context.request().post("/api/auth/login",
                RequestOptions.create().setData(Map.of("username", login, "password", password)));

        if (!response.ok())
            throw new IllegalStateException("API sign-in as '%s' failed: %d %s"
                    .formatted(login, response.status(), response.text()));

        JsonObject user = JsonParser.parseString(response.text())
                .getAsJsonObject().getAsJsonObject("data").getAsJsonObject("user");

        JsonObject authState = new JsonObject();
        authState.add("user", user);
        authState.addProperty("isAuthenticated", true);
        JsonObject authStorage = new JsonObject();
        authStorage.add("state", authState);
        authStorage.addProperty("version", 0);

        // only seed when absent, so a sign-out during the scenario isn't undone by the next navigation
        context.addInitScript("""
                if (!localStorage.getItem('auth-storage'))
                    localStorage.setItem('auth-storage', %s);
                """.formatted(new JsonPrimitive(authStorage.toString()))
        );
    }

}
