package infrastructure.utilities.asserters;

import org.opentest4j.AssertionFailedError;

/**
 * Adds a custom message to Playwright assertions, which (unlike the JS version) don't accept one
 */
public final class Asserter {

    private Asserter() {}

    /**
     * Runs the assertion and prefixes its failure with the given message.
     * Playwright's own message, call log and expected/actual values are kept.
     *
     * @param message what was expected, in business terms
     * @param assertion e.g. {@code () -> assertThat(locator).hasText("...")}
     */
    public static void verify(String message, Runnable assertion) {
        try {
            assertion.run();
        } catch (AssertionFailedError e) {
            throw e.isExpectedDefined() || e.isActualDefined()
                    ? new AssertionFailedError(message + "\n" + e.getMessage(),
                            e.getExpected() == null ? null : e.getExpected().getValue(),
                            e.getActual() == null ? null : e.getActual().getValue(),
                            e)
                    : new AssertionFailedError(message + "\n" + e.getMessage(), e);
        } catch (AssertionError e) {
            throw new AssertionError(message + "\n" + e.getMessage(), e);
        }
    }
}
