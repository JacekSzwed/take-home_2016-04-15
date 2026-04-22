package swaglabs.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class ProductsPage {

    private final Page page;
    public final Locator inventoryContainer;
    public final Locator inventoryItems;
    public final Locator sortDropdown;
    public final Locator cartLink;
    public final Locator cartBadge;

    public ProductsPage(Page page) {
        this.page = page;
        this.inventoryContainer = page.getByTestId("inventory-container");
        this.inventoryItems = page.getByTestId("inventory-item");
        this.sortDropdown = page.getByTestId("product-sort-container");
        this.cartLink = page.getByTestId("shopping-cart-link");
        this.cartBadge = page.getByTestId("shopping-cart-badge");
    }

    public void sortBy(String value) {
        sortDropdown.selectOption(value);
    }

    public void addToCartByName(String productName) {
        Locator item = inventoryItems.filter(new Locator.FilterOptions().setHasText(productName));
        item.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart")).click();
    }

    public List<String> getItemNames() {
        return page.getByTestId("inventory-item-name").allTextContents();
    }

    public List<Double> getItemPrices() {
        return page.getByTestId("inventory-item-price").allTextContents().stream()
                .map(t -> Double.parseDouble(t.replace("$", "")))
                .toList();
    }

    public void goToCart() {
        cartLink.click();
    }
}
