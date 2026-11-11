package pageObject.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import pageObject.components.ProductCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Getter
public class CatalogPage {
    private final Page page;
    private final Locator root;
    private final Locator title;
    private final Locator productCount;
    private final Locator productGrid;
    private final Locator productCards;
    private final Locator availableProductCards;
    private final Locator outOfStockProductCards;
    private final Locator cartButton;
    private final Locator cartButtonCount;

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
        this.cartButton = page.getByTestId("navbar-cart-link");
        this.cartButtonCount = cartButton.getByTestId("navbar-cart-badge");
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

    /**
     * Picks {@code count} distinct random in-stock products. Cards are located by name, so they stay stable if the grid re-renders
     */
    public final List<ProductCard> randomAvailableProductCards(int count) {
        // count() doesn't wait, so wait for the grid to render first
        availableProductCards.first().waitFor();
        List<String> productNames = new ArrayList<>(availableProductCards.all().stream()
                .map(card -> new ProductCard(card).getName().textContent().trim())
                .toList());

        if (productNames.size() < count) {
            throw new IllegalStateException("Requested %d available products, but only %d are in stock"
                    .formatted(count, productNames.size()));
        }

        Collections.shuffle(productNames);
        return productNames.subList(0, count).stream().map(this::productCard).toList();
    }

}
