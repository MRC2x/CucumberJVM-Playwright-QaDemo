package steps;

import com.microsoft.playwright.Page;
import infrastructure.testContext.TestContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pageObject.components.ProductCard;
import pageObject.pages.CatalogPage;
import pageObject.pages.HomePage;

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

    @When("user clicks on {string} item")
    public void userClicksOnItem(String itemName) {
        ProductCard item = catalogPage.productCard(itemName);
        item.getRoot().click();
    }

    @When("user clicks on {string} button on the Home page")
    public void userClicksOnButtonOnTheHomePage(String buttonName) {
        homePage.getButtonByName(buttonName).click();
    }
}
