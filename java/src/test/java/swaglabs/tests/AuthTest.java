package swaglabs.tests;

import org.junit.jupiter.api.Test;
import swaglabs.BaseTest;
import swaglabs.pages.LoginPage;
import swaglabs.pages.ProductsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AuthTest extends BaseTest {

    @Test
    void successfulLoginRedirectsToInventory() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("standard_user", "secret_sauce");

        assertThat(page).hasURL(java.util.regex.Pattern.compile("inventory"));
        assertThat(page.getByTestId("inventory-container")).isVisible();
    }

    @Test
    void lockedOutUserSeesErrorMessage() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("locked_out_user", "secret_sauce");

        assertThat(loginPage.errorMessage).containsText("Sorry, this user has been locked out");
    }

    @Test
    void sessionPersistsAfterPageReload() {
        loginAsStandardUser();
        ProductsPage productsPage = new ProductsPage(page);

        assertThat(page).hasURL(java.util.regex.Pattern.compile("inventory"));

        page.reload();

        assertThat(page).hasURL(java.util.regex.Pattern.compile("inventory"));
        assertThat(productsPage.inventoryContainer).isVisible();
    }
}
