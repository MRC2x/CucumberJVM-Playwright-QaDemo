package steps;

import com.microsoft.playwright.Page;
import infrastructure.testContext.TestContext;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static infrastructure.utilities.asserters.Asserter.verify;

@RequiredArgsConstructor
public class CommonSteps {

    private final TestContext testContext;
    private final Page page;

    @Then("user is on the {string} page")
    public void userIsOnThePage(String pageUrl) {
        verify("User should be on the '%s' page".formatted(pageUrl),
                () -> assertThat(page).hasURL(pageUrl));
    }
}
