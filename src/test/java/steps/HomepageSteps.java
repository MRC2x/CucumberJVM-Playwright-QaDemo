package steps;

import com.microsoft.playwright.Page;
import infrastructure.testContext.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pageObject.pages.CatalogPage;
import pageObject.pages.HomePage;

import static infrastructure.constants.WebEnvParams.getLoginParam;
import static infrastructure.constants.WebEnvParams.getPasswordParam;
import static infrastructure.utilities.playwright.utilities.BrowserContextUtils.signIn;

@RequiredArgsConstructor
public class HomepageSteps {

    private final TestContext testContext;
    private final Page page;
    private final HomePage homePage;
    private final CatalogPage catalogPage;

    @Given("a signed-out user on the {string} page")
    public void aSignedOutUserOnThePage(String path) {
        page.navigate(path);
    }

    @Given("a signed-in user on the {string} page")
    public void aSignedInUserOnThePage(String path) {
        signIn(page.context(), getLoginParam(), getPasswordParam());
        page.navigate(path);
    }

    @When("user clicks on {string} button on the Home page")
    public void userClicksOnButtonOnTheHomePage(String buttonName) {
        homePage.getButtonByName(buttonName).click();
    }

}
