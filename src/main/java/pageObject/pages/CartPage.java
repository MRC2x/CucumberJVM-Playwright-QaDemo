package pageObject.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;


@Getter
public class CartPage {
    @Getter(AccessLevel.NONE)
    private final Page page;
    private final Locator root;
    private final Locator clearCartButton;
    private final Locator proceedToCheckoutButton;
    private final Locator continueShoppingButton;
    private final Locator totalPrice;
    private final Locator cartItems;

    public CartPage(Page page) {
        this.page = page;
        this.root = page.getByTestId("cart-page");
        this.clearCartButton = root.getByTestId("clear-cart-button");
        this.proceedToCheckoutButton = root.getByTestId("proceed-to-checkout-button");
        this.continueShoppingButton = root.getByTestId("continue-shopping-button");
        this.totalPrice = root.getByTestId("order-total");
        this.cartItems = root.locator("[data-testid^='cart-items-list'] > div");
    }

    public Locator getButtonByName(String buttonName) {
        return switch (buttonName) {
            case "Clear Cart" -> clearCartButton;
            case "Proceed to Checkout" -> proceedToCheckoutButton;
            case "Continue Shopping" -> continueShoppingButton;
            default -> throw new IllegalArgumentException("No button found with name: " + buttonName);
        };
    }



}
