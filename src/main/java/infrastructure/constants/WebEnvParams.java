package infrastructure.constants;


import java.util.Locale;

public class WebEnvParams {

    private static final String browserValue = "chromium";


    public static String getBrowserParam() {
        return System.getProperty("browser", browserValue).toLowerCase(Locale.ROOT);
    }
}
