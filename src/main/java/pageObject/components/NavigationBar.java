package pageObject.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;

/**
 * Top navigation bar shared by all pages
 */
@Getter
public class NavigationBar {
    private final Locator root;
    private final Locator logo;
    private final Locator productsLink;
    private final Locator cartLink;
    private final Locator signInLink;
    private final Locator signUpLink;
    private final Locator mobileMenuButton;

    public NavigationBar(Page page) {
        this.root = page.getByTestId("navbar");
        this.logo = root.getByTestId("navbar-logo");
        this.productsLink = root.getByTestId("navbar-products-link");
        this.cartLink = root.getByTestId("navbar-cart-link");
        this.signInLink = root.getByTestId("navbar-signin-link");
        this.signUpLink = root.getByTestId("navbar-signup-link");
        this.mobileMenuButton = root.getByTestId("navbar-mobile-menu-button");
    }
}
