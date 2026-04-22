package swaglabs.tests;

import com.microsoft.playwright.Route;
import org.junit.jupiter.api.Test;
import swaglabs.BaseTest;
import swaglabs.pages.LoginPage;
import swaglabs.pages.ProductsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NetworkTest extends BaseTest {

    @Test
    void inventoryPageWorksWhenImagesFailToLoad() {
        // Block all image requests to simulate network issues
        page.route("**/*.{png,jpg,jpeg,svg}", Route::abort);

        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        ProductsPage productsPage = new ProductsPage(page);
        assertThat(productsPage.inventoryContainer).isVisible();
        assertFalse(productsPage.getItemNames().isEmpty());
    }
}
