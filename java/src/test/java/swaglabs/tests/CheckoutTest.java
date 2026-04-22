package swaglabs.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swaglabs.BaseTest;
import swaglabs.pages.CartPage;
import swaglabs.pages.CheckoutPage;
import swaglabs.pages.ProductsPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class CheckoutTest extends BaseTest {

    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @BeforeEach
    void loginAndInitPages() {
        loginAsStandardUser();
        productsPage = new ProductsPage(page);
        cartPage = new CartPage(page);
        checkoutPage = new CheckoutPage(page);
    }

    @Test
    void completePurchaseFlowFromLoginToOrderConfirmation() {
        productsPage.addToCartByName("Sauce Labs Backpack");
        productsPage.goToCart();

        assertThat(cartPage.cartItems).hasCount(1);
        cartPage.checkout();

        checkoutPage.fillInfo("Jan", "Kowalski", "00-001");

        assertThat(page).hasURL(Pattern.compile("checkout-step-two"));
        assertThat(page.getByTestId("inventory-item-name")).hasText("Sauce Labs Backpack");
        checkoutPage.finish();

        assertThat(page).hasURL(Pattern.compile("checkout-complete"));
        assertThat(checkoutPage.completeHeader).hasText("Thank you for your order!");
    }
}
