package infrastructure.constants;


import java.util.Locale;

public class WebEnvParams {

    private static final String browserValue = "chromium";


    public static String getBrowserParam() {
        return System.getProperty("browser", browserValue).toLowerCase(Locale.ROOT);
    }

    public static String getLoginParam() {
        return getRequiredParam("login");
    }

    public static String getPasswordParam() {
        return getRequiredParam("password");
    }

    private static String getRequiredParam(String name) {
        String value = System.getProperty(name);

        if (value == null || value.isBlank())
            throw new IllegalStateException("System property '%s' is not set, pass it as -D%s=...".formatted(name, name));

        return value;
    }
}
