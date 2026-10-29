package steps;

import com.microsoft.playwright.Locator;
import infrastructure.testContext.TestContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pageObject.pages.CartPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static infrastructure.utilities.asserters.Asserter.verify;

@RequiredArgsConstructor
public class CartSteps {

    private final TestContext testContext;
    private final CartPage cartPage;

    @When("selected item is added to the cart")
    public void selectedItemIsAddedToTheCart() {
        String selectedProductName = testContext.getSelectedProductName();

        verify("Cart should contain the selected product '%s'".formatted(selectedProductName),
            () -> assertThat(cartPage.getCartItems()).hasText(Pattern.compile(selectedProductName))
        );
    }

    @Then("user clicks on {string} button on the Cart page")
    public void userClicksOnButtonOnTheCartPage(String buttonName) {
        Locator button = cartPage.getButtonByName(buttonName);

        verify("User should be able to click on the '%s' button".formatted(buttonName),
             () -> assertThat(button).isEnabled()
        );

        button.click();
    }

}
