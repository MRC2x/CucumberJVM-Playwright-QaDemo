package pageObject.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;
import pageObject.components.NavigationBar;
import pageObject.components.ProductCard;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Getter
public class CatalogPage {
    @Getter(AccessLevel.NONE)
    private final Page page;
    private final Locator root;
    private final Locator title;
    private final Locator productCount;
    private final Locator productGrid;
    private final Locator productCards;
    private final Locator availableProductCards;
    private final Locator outOfStockProductCards;

    public CatalogPage(Page page) {
        this.page = page;
        this.root = page.getByTestId("catalog-page");
        this.title = root.getByTestId("catalog-heading");
        this.productCount = root.getByTestId("catalog-product-count");
        this.productGrid = root.getByTestId("product-grid");
        this.productCards = productGrid.locator("[data-testid^='product-card-']");

        // inner locator of has/hasNot is matched inside each card, so it must be built from page, not from root
        Locator outOfStockBadge = page.locator("[data-testid^='product-out-of-stock-badge-']");
        this.availableProductCards = productCards.filter(new Locator.FilterOptions().setHasNot(outOfStockBadge));
        this.outOfStockProductCards = productCards.filter(new Locator.FilterOptions().setHas(outOfStockBadge));
    }


    /**
     * @param productName exact product name as displayed on the card
     */
    public final ProductCard productCard(String productName) {
        return ProductCard.byName(productCards, productName);
    }

    public final List<ProductCard> allProductCards() {
        return productCards.all().stream().map(ProductCard::new).toList();
    }

    /**
     * Picks a random in-stock product. The card is located by its name, so it stays stable if the grid re-renders
     */
    public final ProductCard randomAvailableProductCard() {
        // count() doesn't wait, so wait for the grid to render first
        availableProductCards.first().waitFor();
        int index = ThreadLocalRandom.current().nextInt(availableProductCards.count());
        String productName = new ProductCard(availableProductCards.nth(index)).getName().textContent().trim();

        return productCard(productName);
    }

}
