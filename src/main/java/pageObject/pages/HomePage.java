package pageObject.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;
import pageObject.components.NavigationBar;
import pageObject.components.ProductCard;


@Getter
public class HomePage {
    @Getter(AccessLevel.NONE)
    private final Page page;
    private final NavigationBar navigationBar;
    private final Locator browseProductsButton;
    private final Locator webMcpButton;
    private final Locator userMenuButton;
    private final Locator logOutButton;
    private final Locator productCards;
    private final ProductCard laptopBackpackItem;

    public HomePage(Page page) {
        this.page = page;
        this.navigationBar = new NavigationBar(page);
        this.browseProductsButton = page.getByTestId("browse-products-button");
        this.webMcpButton = page.getByTestId("webmcp-button");
        this.userMenuButton = page.getByTestId("navbar-username-link");
        this.logOutButton = page.getByTestId("navbar-logout-button");
        this.productCards = page.getByTestId("home-page").locator("[data-testid^='product-card-']");
        this.laptopBackpackItem = productCard("Laptop Backpack");
    }

    public Locator getButtonByName(String buttonName) {
        return switch (buttonName) {
            case "Browse Products" -> browseProductsButton;
            case "Web MCP" -> webMcpButton;
            case "User Menu" -> userMenuButton;
            case "Log Out" -> logOutButton;
            case "Cart" -> navigationBar.getCartLink();
            case "Sign In" -> navigationBar.getSignInLink();
            case "Sign Up" -> navigationBar.getSignUpLink();
            default -> throw new IllegalArgumentException("No button found with name: " + buttonName);
        };
    }

    public ProductCard productCard(String productName) {
        return ProductCard.byName(productCards, productName);
    }

}
