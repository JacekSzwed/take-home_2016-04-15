package swaglabs.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaglabs.BaseTest;
import swaglabs.pages.CartPage;
import swaglabs.pages.ProductsPage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductsCartTest extends BaseTest {

    ProductsPage productsPage;
    CartPage cartPage;

    @BeforeEach
    void loginAndInitPages() {
        loginAsStandardUser();
        productsPage = new ProductsPage(page);
        cartPage = new CartPage(page);
    }

    @Test
    void productsSortCorrectlyByAllOptions() {
        // A-Z (default)
        productsPage.sortBy("az");
        List<String> namesAZ = productsPage.getItemNames();
        ArrayList<String> sortedAZ = new ArrayList<>(namesAZ);
        sortedAZ.sort(String::compareTo);
        assertEquals(sortedAZ, namesAZ);

        // Z-A
        productsPage.sortBy("za");
        List<String> namesZA = productsPage.getItemNames();
        ArrayList<String> sortedZA = new ArrayList<>(namesZA);
        sortedZA.sort(Comparator.reverseOrder());
        assertEquals(sortedZA, namesZA);

        // Price low to high
        productsPage.sortBy("lohi");
        List<Double> pricesLow = productsPage.getItemPrices();
        ArrayList<Double> sortedLow = new ArrayList<>(pricesLow);
        sortedLow.sort(Double::compareTo);
        assertEquals(sortedLow, pricesLow);

        // Price high to low
        productsPage.sortBy("hilo");
        List<Double> pricesHigh = productsPage.getItemPrices();
        ArrayList<Double> sortedHigh = new ArrayList<>(pricesHigh);
        sortedHigh.sort(Comparator.reverseOrder());
        assertEquals(sortedHigh, pricesHigh);
    }

    @Test
    void addingOneProductUpdatesCartBadge() {
        assertThat(productsPage.cartBadge).not().isVisible();

        productsPage.addToCartByName("Sauce Labs Backpack");

        assertThat(productsPage.cartBadge).hasText("1");
    }

    @Test
    void addingMultipleProductsShowsCorrectItemsInCart() {
        List<String> products = List.of("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt");

        for (String product : products) {
            productsPage.addToCartByName(product);
        }
        assertThat(productsPage.cartBadge).hasText("3");

        productsPage.goToCart();
        List<String> cartItems = cartPage.getItemNames();
        assertEquals(products.size(), cartItems.size());
        assertTrue(cartItems.containsAll(products));
    }

    @Test
    void removingProductFromCart() {
        productsPage.addToCartByName("Sauce Labs Backpack");
        productsPage.addToCartByName("Sauce Labs Bike Light");
        productsPage.goToCart();

        cartPage.removeByName("Sauce Labs Backpack");

        List<String> remaining = cartPage.getItemNames();
        assertEquals(List.of("Sauce Labs Bike Light"), remaining);
    }
}
