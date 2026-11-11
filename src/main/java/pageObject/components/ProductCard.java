package pageObject.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public class ProductCard {
    private final Locator root;
    private final Locator link;
    private final Locator image;
    private final Locator outOfStockBadge;
    private final Locator lowStockBadge;
    private final Locator name;
    private final Locator description;
    private final Locator price;
    private final Locator addToCartButton;
    private final Locator removeFromCartButton;

    public ProductCard(Locator root) {
        this.root = root;
        this.link = root.locator("[data-testid^='product-link-']");
        this.image = root.locator("[data-testid^='product-image-']");
        this.outOfStockBadge = root.locator("[data-testid^='product-out-of-stock-badge-']");
        this.lowStockBadge = root.locator("[data-testid^='product-low-stock-badge-']");
        this.name = root.locator("[data-testid^='product-name-']");
        this.description = root.locator("[data-testid^='product-description-']");
        this.price = root.locator("[data-testid^='product-price-']");
        this.addToCartButton = root.locator("[data-testid^='product-add-to-cart-']");
        this.removeFromCartButton = root.locator("[data-testid^='product-remove-from-cart-']");
    }

    /**
     * @param productCards locator matching all product cards in a container
     * @param productName exact product name as displayed on the card
     */
    public static ProductCard byName(Locator productCards, String productName) {
        return new ProductCard(productCards.filter(new Locator.FilterOptions()
                .setHas(productCards.page().getByRole(AriaRole.HEADING,
                        new Page.GetByRoleOptions().setName(productName).setExact(true)))));
    }
}
