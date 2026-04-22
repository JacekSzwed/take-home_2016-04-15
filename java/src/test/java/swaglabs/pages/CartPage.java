package swaglabs.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class CartPage {

    private final Page page;
    public final Locator cartList;
    public final Locator cartItems;
    public final Locator checkoutButton;
    public final Locator continueShoppingButton;

    public CartPage(Page page) {
        this.page = page;
        this.cartList = page.getByTestId("cart-list");
        this.cartItems = page.getByTestId("inventory-item");
        this.checkoutButton = page.getByTestId("checkout");
        this.continueShoppingButton = page.getByTestId("continue-shopping");
    }

    public void removeByName(String productName) {
        Locator item = cartItems.filter(new Locator.FilterOptions().setHasText(productName));
        item.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove")).click();
    }

    public List<String> getItemNames() {
        return page.getByTestId("inventory-item-name").allTextContents();
    }

    public void checkout() {
        checkoutButton.click();
    }
}
