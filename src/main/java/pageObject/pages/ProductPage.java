package pageObject.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;


@Getter
public class ProductPage {
    @Getter(AccessLevel.NONE)
    private final Page page;
    private final Locator root;
    private final Locator backToCatalogLink;
    private final Locator productImage;
    private final Locator productName;
    private final Locator productPrice;
    private final Locator productDescription;
    private final Locator addToCartButton;
    private final Locator inCartButton;
    private final Locator viewCartButton;

    public ProductPage(Page page) {
        this.page = page;
        this.root = page.getByTestId("product-page");
        this.backToCatalogLink = root.getByTestId("product-back-to-catalog");
        this.productImage = root.getByTestId("product-detail-image");
        this.productName = root.getByTestId("product-detail-name");
        this.productPrice = root.getByTestId("product-detail-price");
        this.productDescription = root.getByTestId("product-detail-description");
        this.addToCartButton = root.getByTestId("product-add-to-cart-button");
        this.inCartButton = root.getByTestId("product-remove-from-cart-button");
        this.viewCartButton = root.getByTestId("product-view-cart-button");
    }

    public Locator getButtonByName(String buttonName) {
        return switch (buttonName) {
            case "Add to Cart" -> addToCartButton;
            case "In Cart" -> inCartButton;
            case "View Cart" -> viewCartButton;
            default -> throw new IllegalArgumentException("No button found with name: " + buttonName);
        };
    }

}
