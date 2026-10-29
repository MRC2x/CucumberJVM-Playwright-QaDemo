package steps;

import infrastructure.testContext.TestContext;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import pageObject.components.ProductCard;
import pageObject.pages.CatalogPage;

@RequiredArgsConstructor
public class CatalogSteps {

    private final TestContext testContext;
    private final CatalogPage catalogPage;

    @When("user selects any available item from catalog")
    public void userSelectsAnyAvailableItemFromCatalog() {
        ProductCard item = catalogPage.randomAvailableProductCard();
        testContext.setSelectedProductName(item.getName().textContent().trim());
        item.getLink().click();
    }
}
