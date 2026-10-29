package steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import infrastructure.testContext.TestContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import pageObject.pages.ProductPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static infrastructure.utilities.asserters.Asserter.verify;

@RequiredArgsConstructor
public class ProductSteps {

    private final TestContext testContext;
    private final Page page;
    private final ProductPage productPage;


    @Then("user is on the product page of the selected item")
    public void userIsOnTheProductPageOfTheSelectedItem() {
        String itemName = testContext.getSelectedProductName();

        verify("'%s' product page should be opened".formatted(itemName),
                () -> assertThat(page).hasURL(Pattern.compile("/products/" + itemName.toLowerCase().replace(' ', '-') + "$"))
        );
        verify("Product page should show '%s' as the product name".formatted(itemName),
                () -> assertThat(productPage.getProductName()).hasText(itemName)
        );
    }

    @And("user clicks on {string} button on the Product page")
    public void userClicksOnAddToCartButton(String buttonName) {
        Locator button = productPage.getButtonByName(buttonName);

        verify("Product page should have the '%s' button enabled".formatted(buttonName),
                () -> assertThat(button).isEnabled()
        );

        button.click();;
    }

}
