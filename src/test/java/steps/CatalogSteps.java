package steps;

import infrastructure.testContext.TestContext;
import infrastructure.utilities.logging.Log;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pageObject.components.ProductCard;
import pageObject.pages.CatalogPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static infrastructure.utilities.asserters.Asserter.verify;

@RequiredArgsConstructor
public class CatalogSteps {

    private final TestContext testContext;
    private final CatalogPage catalogPage;

    @When("user selects any available item from catalog")
    public void userSelectsAnyAvailableItemFromCatalog() {
        ProductCard item = catalogPage.randomAvailableProductCard();

        String itemName = item.getName().textContent().trim();
        Log.info("Selecting item '%s' from catalog".formatted(itemName));
        testContext.setSelectedProductName(itemName);

        item.getLink().click();
    }

    @When("user randomly adds {int} available item from catalog")
    public void userRandomlyAddsAvailableItemFromCatalog(int numberOfItems) {
        for (ProductCard item : catalogPage.randomAvailableProductCards(numberOfItems)) {
            String itemName = item.getName().textContent().trim();
            Log.info("Adding item '%s' to cart".formatted(itemName));
            testContext.getSelectedProductNames().add(itemName);

            item.getAddToCartButton().click();
        }
    }

    @Then("cart icon shows {int} items in the cart")
    public void cartIconShowsItemsInTheCart(int numberOfItems) {
        if (numberOfItems == 0)
            verify("Cart button counter should not be visible",
                    () -> assertThat(catalogPage.getCartButtonCount()).not().isVisible()
            );
        else
            verify("Cart button counter should show '%s' items".formatted(numberOfItems),
                    () -> assertThat(catalogPage.getCartButtonCount()).hasText(String.valueOf(numberOfItems))
            );
    }

    @When("user clicks on the cart icon on the Catalog page")
    public void userClicksOnTheCartIconOnTheCatalogPage() {
        catalogPage.getCartButton().click();
    }

    @And("X button is present and active on the card of the added item")
    public void xButtonIsPresentAndActiveOnTheCardOfTheAddedItem() {
        String selectedProductName = testContext.getSelectedProductName();
        ProductCard item = catalogPage.productCard(selectedProductName);

        verify("'X' button should be visible on the item card",
                () -> assertThat(item.getRemoveFromCartButton()).isVisible()
        );

        verify("'X' button should be visible on the item card",
                () -> assertThat(item.getRemoveFromCartButton()).isEnabled()
        );
    }

    @When("user clicks on the X button on the added item card")
    public void userClicksOnTheXButtonOnTheAddedItemCard() {
        String selectedProductName = testContext.getSelectedProductName();
        ProductCard item = catalogPage.productCard(selectedProductName);

        item.getRemoveFromCartButton().click();
    }
}
